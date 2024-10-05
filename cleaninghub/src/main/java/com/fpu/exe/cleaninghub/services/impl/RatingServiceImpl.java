package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.entity.Rating;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.RatingRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }


    private Double calculateAverageRating(Integer staffId) {
        List<Rating> ratingsOfStaff = ratingRepository.findAllByStaffId(staffId);
        if (ratingsOfStaff == null || ratingsOfStaff.isEmpty()) {
            return userRepository.findById(staffId).get().getAverageRating() + 0.0;
        }
        double totalStars = ratingsOfStaff.stream()
                .mapToInt(Rating::getStars)
                .sum();

        return totalStars / ratingsOfStaff.size();
    }

    @Override
    @Scheduled(cron = "0 */2 * * * *")
    public void updateAverageRating() {
        List<User> users = userRepository.findAllStaff();
        users.forEach(user -> {
            Double averagePoint = calculateAverageRating(user.getId());
            user.setAverageRating(averagePoint);
            userRepository.save(user);
        });
    }

    @Override
    public Integer numberOfRatings(Integer staffId) {
        List<Rating> listRatingOfStaff = ratingRepository.findAllByStaffId(staffId);
        return listRatingOfStaff.size();
    }

    @Override
    public List<User> getFiveUserHaveHighestAverageRating() {
        List<User> list = userRepository.getFiveUserHaveHighestAverageRating();
        return list;
    }


}
