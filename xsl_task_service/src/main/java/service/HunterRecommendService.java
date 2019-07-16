package service;

import java.util.List;

public interface HunterRecommendService {

    /**
     * 猎人推荐
     *
     * @param taskId
     * @return 猎人id数组
     */
    List<String> recommend(String taskId, Integer recommendNum);

}
