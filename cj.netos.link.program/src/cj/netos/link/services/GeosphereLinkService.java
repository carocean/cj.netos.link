package cj.netos.link.services;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.link.AbstractLinkService;
import cj.netos.link.IGeosphereLinkService;
import cj.netos.link.entities.Channel;
import cj.netos.link.entities.geo.*;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.ultimate.gson2.com.google.gson.Gson;
import cj.ultimate.util.StringUtil;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;

import java.util.*;

@CjService(name = "geosphereLinkService")
public class GeosphereLinkService extends AbstractLinkService implements IGeosphereLinkService {
    final static String _KEY_CATEGORY_COL = "geo.categories";

    @CjServiceRef(refByName = "mongodb.netos.home")
    ICube home;

    String _getReceptorColName() {
        return String.format("geo.receptors");
    }

    String _getDocumentColName() {
        return String.format("geo.receptor.docs");
    }

    String _getFollowColName() {
        return String.format("geo.receptor.follows");
    }

    @Override
    public GeoReceptor getReceptor(String receptor) {
        String cjql = "select {'tuple':'*'} from tuple ?(colname) ?(clazz) where {'tuple.id':'?(receptor)'}";
        IQuery<GeoReceptor> query = home.createQuery(cjql);
        query.setParameter("colname", _getReceptorColName());
        query.setParameter("clazz", GeoReceptor.class.getName());
        query.setParameter("receptor", receptor);
        IDocument<GeoReceptor> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    public List<GeoCategory> listCategory() {
        String cjql = "select {'tuple':'*'} from tuple ?(colname) ?(clazz) where {}";
        IQuery<GeoCategory> query = home.createQuery(cjql);
        query.setParameter("colname", _KEY_CATEGORY_COL);
        query.setParameter("clazz", GeoCategory.class.getName());
        List<IDocument<GeoCategory>> list = query.getResultList();
        List<GeoCategory> categories = new ArrayList<>();
        for (IDocument<GeoCategory> doc : list) {
            categories.add(doc.tuple());
        }
        return categories;
    }

    public List<GeoCategory> findCategories(String geoType) {
        String[] arr = geoType.split("\\|");
        List<String> ids = new ArrayList();
        for (String id : arr) {
            if (StringUtil.isEmpty(id)) {
                continue;
            }
            ids.add(id);
        }
        String cjql = "select {'tuple':'*'} from tuple ?(colname) ?(clazz) where {'tuple.id':{'$in':?(ids)}}";
        IQuery<GeoCategory> query = home.createQuery(cjql);
        query.setParameter("colname", _KEY_CATEGORY_COL);
        query.setParameter("clazz", GeoCategory.class.getName());
        query.setParameter("ids", new Gson().toJson(ids));
        List<IDocument<GeoCategory>> list = query.getResultList();
        List<GeoCategory> categories = new ArrayList<>();
        for (IDocument<GeoCategory> doc : list) {
            categories.add(doc.tuple());
        }
        return categories;
    }

//    public GeoReceptor getMyMobileReceptor(String person) {
//        String cjql = "select {'tuple':'*'}.limit(1) from tuple ?(colname) ?(clazz) where {'tuple.creator':'?(creator)'}";
//        IQuery<GeoReceptor> query = home.createQuery(cjql);
//        query.setParameter("colname", _getReceptorColName("mobiles"));
//        query.setParameter("clazz", GeoReceptor.class.getName());
//        query.setParameter("creator", person);
//        IDocument<GeoReceptor> doc = query.getSingleResult();
//        if (doc == null) {
//            return null;
//        }
//        return doc.tuple();
//    }

    @Override
    public List<GeoPOI> searchAroundLocation(String principal, LatLng location, double radius, String geoType, long limit, long skip) {
        List<String> categoryIds = new ArrayList<>();
        if (!StringUtil.isEmpty(geoType)) {
            String[] arr = geoType.split("\\|");
            for (String t : arr) {
                categoryIds.add(t);
            }
        }
        List<GeoPOI> pois = _searchPoiInCategory(location, radius, categoryIds, limit, skip);
        return pois;
    }

    @Override
    public List<GeoPOI> searchPersonAroundLocation(String principal, LatLng location, double radius, String geoType, String person) {
        LatLng latLng = location;
        List<String> categoryIds = new ArrayList<>();
        if (!StringUtil.isEmpty(geoType)) {
            String[] arr = geoType.split("\\|");
            for (String t : arr) {
                categoryIds.add(t);
            }
        }
        List<GeoPOI> pois = _searchPoiInCategoryWherePerson(latLng, radius, categoryIds, person);
        return pois;
    }

    @Override
    public List<GeoPOI> searchAroundReceptors(String person, GeoReceptor geoReceptor, String geoType, long limit, long skip) {
        List<String> categoryIds = new ArrayList<>();
        if (!StringUtil.isEmpty(geoType)) {
            String[] arr = geoType.split("\\|");
            for (String t : arr) {
                categoryIds.add(t);
            }
        }
        LatLng latLng = geoReceptor.getLocation();
        double radius = geoReceptor.getRadius();
        List<GeoPOI> pois = _searchPoiInCategory(latLng, radius, categoryIds, limit, skip);
        return pois;
    }

    private List<GeoPOI> _searchPoiInCategory(LatLng location, double radius, List<String> categoryIds, long limit, long skip) {
        //distanceField:"distance" 距离字段别称
        //"distanceMultiplier": 0.001,
        //{ $limit : 5 }
        String json = String.format("{" +
                "'$geoNear':{" +
                "'near':{'type':'Point','coordinates':%s}," +
                "'distanceField':'tuple.distance'," +
                "'maxDistance':%s," +
                "'spherical':true" +
                "}" +
                "}", location.toCoordinate(), radius);

        String limitjson = String.format("{'$limit':%s}", limit);
        String skipjson = String.format("{'$skip':%s}", skip);
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(Document.parse(json));
        if (!categoryIds.isEmpty()) {
            String matchjson = String.format("{'$match':{'tuple.category':{'$in':%s}}}", new Gson().toJson(categoryIds));
            pipeline.add(Document.parse(matchjson));
        }
        pipeline.add(Document.parse(limitjson));
        pipeline.add(Document.parse(skipjson));
        AggregateIterable<Document> it = home.aggregate(_getReceptorColName(), pipeline);
        List<GeoPOI> list = new ArrayList<>();
        for (Document doc : it) {
            GeoPOI poi = GeoPOI.parse(doc);
            list.add(poi);
        }
        return list;
    }

    private List<GeoPOI> _searchPoiInCategoryWherePerson(LatLng location, double radius, List<String> categoryIds, String person) {
        //distanceField:"distance" 距离字段别称
        //"distanceMultiplier": 0.001,
        //{ $limit : 5 }
        String json = String.format("{" +
                "'$geoNear':{" +
                "'near':{'type':'Point','coordinates':%s}," +
                "'distanceField':'tuple.distance'," +
                "'maxDistance':%s," +
                "'spherical':true" +
                "}" +
                "}", location.toCoordinate(), radius);
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(Document.parse(json));
        if (categoryIds.isEmpty()) {
            String matchjson = String.format("{'$match':{'tuple.creator':'%s'}}", person);
            pipeline.add(Document.parse(matchjson));
        } else {
            String matchjson = String.format("{'$match':{'tuple.creator':'%s','tuple.category':{'$in':%s}}}", person, new Gson().toJson(categoryIds));
            pipeline.add(Document.parse(matchjson));
        }
        AggregateIterable<Document> it = home.aggregate(_getReceptorColName(), pipeline);
        List<GeoPOI> list = new ArrayList<>();
        for (Document doc : it) {
            GeoPOI poi = GeoPOI.parse(doc);
            list.add(poi);
        }
        return list;
    }


    @Override
    public List<GeoPOD> searchAroundDocuments(String person, GeoReceptor geoReceptor, String geoType, long limit, long skip) {
        List<String> categoryIds = new ArrayList<>();
        if (!StringUtil.isEmpty(geoType)) {
            String[] arr = geoType.split("\\|");
            for (String t : arr) {
                categoryIds.add(t);
            }
        }
        LatLng latLng = geoReceptor.getLocation();
        double radius = geoReceptor.getRadius();
        List<GeoPOD> pods = _searchPoDInCategory(latLng, radius, categoryIds, limit, skip);
        return pods;
    }

    private List<GeoPOD> _searchPoDInCategory(LatLng location, double radius, List<String> categoryIds, long limit, long skip) {
        //distanceField:"distance" 距离字段别称
        //"distanceMultiplier": 0.001,
        //{ $limit : 5 }
        String json = String.format("{" +
                "'$geoNear':{" +
                "'near':{'type':'Point','coordinates':%s}," +
                "'distanceField':'tuple.distance'," +
                "'maxDistance':%s," +
                "'spherical':true" +
                "}" +
                "}", location.toCoordinate(), radius);

        List<Document> pipeline = new ArrayList<>();
        pipeline.add(Document.parse(json));
        if (!categoryIds.isEmpty()) {
            String matchjson = String.format("{'$match':{'tuple.category':{'$in':%s}}}", new Gson().toJson(categoryIds));
            pipeline.add(Document.parse(matchjson));
        }
        String limitjson = String.format("{'$limit':%s}", limit);
        String skipjson = String.format("{'$skip':%s}", skip);
        String sortjson = String.format("{'$sort':{'tuple.distance':1,'tuple.ctime':1}}");
        pipeline.add(Document.parse(limitjson));
        pipeline.add(Document.parse(skipjson));
        pipeline.add(Document.parse(sortjson));
        AggregateIterable<Document> it = home.aggregate(_getDocumentColName(), pipeline);
        List<GeoPOD> list = new ArrayList<>();
        for (Document doc : it) {
            GeoPOD pod = GeoPOD.parse(doc);
            list.add(pod);
        }
        return list;
    }

    @Override
    public void followReceptor(GeoReceptor geoReceptor, String follower) {
        unfollowReceptor(geoReceptor, follower);
        String colname = _getFollowColName();
        GeoFollow follow = new GeoFollow();
        follow.setCategory(geoReceptor.getCategory());
        follow.setReceptor(geoReceptor.getId());
        follow.setPerson(follower);
        follow.setRights("allowSpeak");
        follow.setCtime(System.currentTimeMillis());
        home.saveDoc(colname, new TupleDocument<>(follow));
    }

    @Override
    public void unfollowReceptor(GeoReceptor geoReceptor, String follower) {
        String colname = _getFollowColName();
        home.deleteDocs(colname, String.format("{'tuple.receptor':'%s','tuple.person':'%s'}", geoReceptor.getId(), follower));
    }

    @Override
    public void updateFollowRights(GeoReceptor geoReceptor, String fans, String rights) {
        String colname = _getFollowColName();
        home.updateDocOne(colname,
                Document.parse(String.format("{'tuple.receptor':'%s','tuple.person':'%s'}", geoReceptor.getId(), fans)),
                Document.parse(String.format("{'$set':{'tuple.rights':'%s'}}",rights)));
    }

    @Override
    public boolean isDenyFollowSpeak(GeoReceptor geoReceptor, String principal) {
        String colname = _getFollowColName();
        return home.tupleCount(colname,String.format("{'tuple.receptor':'%s','tuple.person':'%s','tuple.rights':'denySpeak'}",geoReceptor.getId(),principal))>0;
    }

    @Override
    public List<GeoPOF> pageReceptorFans(String person, GeoReceptor geoReceptor, long limit, long skip) {
        String cjql = "select {'tuple':'*'} from tuple ?(colname) ?(clazz) where {'tuple.receptor':'?(receptor)'}";
        IQuery<GeoFollow> query = home.createQuery(cjql);
        query.setParameter("colname", _getFollowColName());
        query.setParameter("clazz", GeoFollow.class.getName());
        query.setParameter("receptor", geoReceptor.getId());
        List<IDocument<GeoFollow>> docs = query.getResultList();
        List<String> ids = new ArrayList<>();
        Map<String, GeoFollow> follows = new HashMap<>();
        for (IDocument<GeoFollow> doc : docs) {
            GeoFollow follow=doc.tuple();
            if (StringUtil.isEmpty(follow.getRights())) {
                follow.setRights("allowSpeak");
            }
            ids.add(follow.getPerson());
            follows.put(follow.getPerson(), follow);
        }
        LatLng latLng = geoReceptor.getLocation();
        //由于粉丝不在感知器半径内，也能收到消息，所以设为最大公里数求粉丝
        double radius = /*geoReceptor.getRadius()*/Long.MAX_VALUE;
        //查询这些用户的mobiles分类下的感知器，并以此作为远近距离排序
        //distanceField:"distance" 距离字段别称
        //"distanceMultiplier": 0.001,
        //{ $limit : 5 }
        String json = String.format("{" +
                "'$geoNear':{" +
                "'near':{'type':'Point','coordinates':%s}," +
                "'distanceField':'tuple.distance'," +
                "'maxDistance':%s," +
                "'spherical':true" +
                "}" +
                "}", latLng.toCoordinate(), radius);
        String match = String.format("{'$match':{'tuple.creator':{'$in':%s}}}", new Gson().toJson(ids));
        AggregateIterable<Document> it = home.aggregate(_getReceptorColName(), Arrays.asList(Document.parse(json), Document.parse(match)));
        List<GeoPOF> list = new ArrayList<>();
        ids.clear();
        for (Document doc : it) {
            Map<String, Object> tuple = (Map<String, Object>) doc.get("tuple");
            double distance = (double) tuple.get("distance");
            String creator = (String) tuple.get("creator");
            if (ids.contains(creator)) {
                //注意，如果一个人有多个mobiles感知器会造成重复记录，因此排除掉
                continue;
            }
            GeoPOF pof = new GeoPOF();
            pof.setDistance(distance);
            pof.setFollow(follows.get(creator));
            list.add(pof);
            ids.add(creator);
        }
        return list;
    }

    @Override
    public long countReceptorFans(GeoReceptor geoReceptor) {
        String where = String.format("{'tuple.receptor':'%s'}", geoReceptor.getId());
        return home.tupleCount(_getFollowColName(), where);
    }

    @Override
    public List<Channel> listReceptorChannels(String principal) {
        ICube cube = super.cube(principal);
        String cjql = "select {'tuple':'*'} from tuple ?(colname) ?(clazz) where {'tuple.creator':'?(creator)','tuple.outGeoSelector':'?(outGeoSelector)'}";
        IQuery<Channel> query = cube.createQuery(cjql);
        query.setParameter("colname", "channels");
        query.setParameter("clazz", Channel.class.getName());
        query.setParameter("outGeoSelector", "true");
        query.setParameter("creator", principal);
        List<IDocument<Channel>> docs = query.getResultList();
        List<Channel> channels = new ArrayList<>();
        for (IDocument<Channel> doc : docs) {
            channels.add(doc.tuple());
        }
        return channels;
    }
}
