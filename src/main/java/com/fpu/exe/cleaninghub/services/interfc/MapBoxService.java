package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.response.StaffDistanceInfo;
import com.fpu.exe.cleaninghub.entity.User;

import java.util.List;

public interface MapBoxService {

    List<StaffDistanceInfo> calculateDistanceBetweenTwoLocation(StringBuilder coordinates, List<User> availableStaffs) throws Exception;
}
