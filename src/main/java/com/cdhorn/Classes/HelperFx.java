package com.cdhorn.Classes;


import com.cdhorn.Interfaces.MapRepository;
import com.cdhorn.Interfaces.RunRepository;
import com.cdhorn.Models.Map;
import com.cdhorn.Models.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class HelperFx {

    @Autowired
    MapRepository mapRepo;
    @Autowired
    RunRepository runRepo;

    public Object getMap(@PathVariable("runId") String runId,
                                    @RequestParam("map_id") String mapId) {
        long myMapId = Long.parseLong(mapId);
        Map myMap = mapRepo.findOne(myMapId);
        long myRunId = Long.parseLong(runId);
        Run myRun = runRepo.findOne(myRunId);
        myRun.setMap(myMap);
        runRepo.save(myRun);
        return myMap;

    }

    public void getRun(@PathVariable("runId") String runId,
                       @RequestParam("mapId") String mapId) {


    }
}
