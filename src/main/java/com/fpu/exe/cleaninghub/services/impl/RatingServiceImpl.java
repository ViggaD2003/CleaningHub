package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.RatingRequest;
import com.fpu.exe.cleaninghub.dto.response.RatingDTO;
import com.fpu.exe.cleaninghub.entity.Booking;
import com.fpu.exe.cleaninghub.entity.Rating;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.BookingRepository;
import com.fpu.exe.cleaninghub.repository.RatingRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
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

    @Override
    public RatingDTO createRating(RatingRequest request) {
        Booking booking = bookingRepository.findById(request.bookingId().intValue()).orElseThrow(() -> new RuntimeException("Booking not found"));
        Rating rating = Rating.builder()
                        .stars(request.stars())
                                .ratingDate(LocalDate.now())
                                        .booking(booking)
                                                .comments(request.comments())
                                                        .build();
        ratingRepository.save(rating);

        return RatingDTO.builder()
                .id(rating.getId())
                .comments(rating.getComments())
                .ratingDate(rating.getRatingDate())
                .stars(rating.getStars())
                .build();
    }

}
