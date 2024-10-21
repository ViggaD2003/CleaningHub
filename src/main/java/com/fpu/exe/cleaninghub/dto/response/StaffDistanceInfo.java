package com.fpu.exe.cleaninghub.dto.response;
import com.fpu.exe.cleaninghub.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StaffDistanceInfo {
    private User staff;
    private double distance;
    private double duration;

    public double getDurationInMinutes() {
        return duration / 60.0;
    }

    // Phương thức trả về distance tính theo km
    public double getDistanceInKm() {
        return distance / 1000.0;
    }

    public double getAverageRating(){
        return staff.getAverageRating();
    }
}
