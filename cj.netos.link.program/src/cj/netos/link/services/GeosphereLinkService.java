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

    String _getReceptorColName(String category) {
        return String.format("geo.receptor.%s", category);
    }

    String _getDocumentColName(String category) {
        return String.format("geo.receptor.%s.docs", category);
    }

    String _getFollowColName(String category) {
        return String.format("geo.receptor.%s.follows", category);
    }

    @Override
    public GeoReceptor getReceptor(String category, String receptor) {
        String cjql = "select {'tuple':'*'} from tuple ?(colname) ?(clazz) where {'tuple.id':'?(receptor)'}";
        IQuery<GeoReceptor> query = home.createQuery(cjql);
        query.setParameter("colname", _getReceptorColName(category));
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
    public List<GeoPOI> searchAroundReceptors(String person, GeoReceptor geoReceptor, String geoType, long limit, long skip) {
        List<GeoCategory> categories = null;
        if (StringUtil.isEmpty(geoType)) {
            categories = listCategory();
        } else {
            categories = findCategories(geoType);
        }
        LatLng latLng = geoReceptor.getLocation();
        double radius = geoReceptor.getRadius();
        List<GeoPOI> list = new ArrayList<>();
        for (GeoCategory category : categories) {
            List<GeoPOI> pois = _searchPoiInCategory(latLng, radius, category, limit, skip);
            list.addAll(pois);
        }
        if (categories.size() > 1) {
            //排序要
            Collections.sort(list, new Comparator<GeoPOI>() {
                @Override
                public int compare(GeoPOI o1, GeoPOI o2) {
                    if (o1.getDistance() == o2.getDistance()) {
                        return 0;
                    }
                    return o1.getDistance() > o2.getDistance() ? 1 : -1;
                }
            });
        }
        return list;
    }

    private List<GeoPOI> _searchPoiInCategory(LatLng location, double radius, GeoCategory category, long limit, long skip) {
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
        AggregateIterable<Document> it = home.aggregate(_getReceptorColName(category.getId()), Arrays.asList(Document.parse(json), Document.parse(limitjson), Document.parse(skipjson)));
        List<GeoPOI> list = new ArrayList<>();
        for (Document doc : it) {
            GeoPOI poi = GeoPOI.parse(doc);
            list.add(poi);
        }
        return list;
    }


    @Override
    public List<GeoPOD> searchAroundDocuments(String person, GeoReceptor geoReceptor, String geoType, long limit, long skip) {
        List<GeoCategory> categories = null;
        if (StringUtil.isEmpty(geoType)) {
            categories = listCategory();
        } else {
            categories = findCategories(geoType);
        }
        LatLng latLng = geoReceptor.getLocation();
        double radius = geoReceptor.getRadius();
        List<GeoPOD> list = new ArrayList<>();
        for (GeoCategory category : categories) {
            List<GeoPOD> pods = _searchPoDInCategory(latLng, radius, category, limit, skip);
            list.addAll(pods);
        }
        if (categories.size() > 1) {
            //排序要
            Collections.sort(list, new Comparator<GeoPOD>() {
                @Override
                public int compare(GeoPOD o1, GeoPOD o2) {
                    if (o1.getDistance() == o2.getDistance()) {
                        return 0;
                    }
                    return o1.getDistance() > o2.getDistance() ? 1 : -1;
                }
            });
        }
        return list;
    }

    private List<GeoPOD> _searchPoDInCategory(LatLng location, double radius, GeoCategory category, long limit, long skip) {
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
        AggregateIterable<Document> it = home.aggregate(_getDocumentColName(category.getId()), Arrays.asList(Document.parse(json), Document.parse(limitjson), Document.parse(skipjson)));
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
        String colname = _getFollowColName(geoReceptor.getCategory());
        GeoFollow follow = new GeoFollow();
        follow.setCategory(geoReceptor.getCategory());
        follow.setReceptor(geoReceptor.getId());
        follow.setPerson(follower);
        follow.setCtime(System.currentTimeMillis());
        home.saveDoc(colname, new TupleDocument<>(follow));
    }

    @Override
    public void unfollowReceptor(GeoReceptor geoReceptor, String follower) {
        String colname = _getFollowColName(geoReceptor.getCategory());
        home.deleteDocs(colname, String.format("{'tuple.category':'%s','tuple.receptor':'%s','tuple.person':'%s'}", geoReceptor.getCategory(), geoReceptor.getId(), follower));
    }

    @Override
    public List<GeoPOF> pageReceptorFans(String person, GeoReceptor geoReceptor, long limit, long skip) {
        String cjql = "select {'tuple':'*'} from tuple ?(colname) ?(clazz) where {'tuple.category':'?(category)','tuple.receptor':'?(receptor)'}";
        IQuery<GeoFollow> query = home.createQuery(cjql);
        query.setParameter("colname", _getFollowColName(geoReceptor.getCategory()));
        query.setParameter("clazz", GeoFollow.class.getName());
        query.setParameter("category", geoReceptor.getCategory());
        query.setParameter("receptor", geoReceptor.getId());
        List<IDocument<GeoFollow>> docs = query.getResultList();
        List<String> ids = new ArrayList<>();
        Map<String, GeoFollow> follows = new HashMap<>();
        for (IDocument<GeoFollow> doc : docs) {
            ids.add(doc.tuple().getPerson());
            follows.put(doc.tuple().getPerson(), doc.tuple());
        }
        LatLng latLng = geoReceptor.getLocation();
        double radius = geoReceptor.getRadius();
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
        AggregateIterable<Document> it = home.aggregate(_getReceptorColName("mobiles"), Arrays.asList(Document.parse(json), Document.parse(match)));
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
