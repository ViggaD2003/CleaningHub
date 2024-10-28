package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.RatingRequest;
import com.fpu.exe.cleaninghub.dto.response.RatingDTO;
import com.fpu.exe.cleaninghub.entity.User;
import java.util.List;

public interface RatingService {

    void updateAverageRating();

    Integer numberOfRatings(Integer staffId);

    List<User> getFiveUserHaveHighestAverageRating();

    RatingDTO createRating(RatingRequest request);
}
