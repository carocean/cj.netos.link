package cj.netos.link.services;

import cj.lns.chip.sos.cube.framework.CubeConfig;
import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.disk.INetDisk;
import cj.studio.ecm.annotation.CjServiceRef;

public class AbstractLinkService {
    @CjServiceRef(refByName = "mongodb.netos")
    INetDisk disk;

    protected ICube cube(String person) {
        if (!disk.existsCube(person)) {
            CubeConfig cubeConfig = new CubeConfig();
            cubeConfig.alias(person);
            return disk.createCube(person, cubeConfig);
        }
        return disk.cube(person);
    }
}
