package com.anish.seatbooking.dtos;

public class AdminDashboardResponse {
    private long totalSeats;
    private long availableSeats;
    private long confirmedSeats;
    private long totalUsers;

    public AdminDashboardResponse(long totalSeats, long availableSeats, long confirmedSeats, long totalUsers) {
        this.availableSeats = availableSeats;
        this.confirmedSeats = confirmedSeats;
        this.totalSeats = totalSeats;
        this.totalUsers = totalUsers;
    }

    public long getTotalSeats() {
        return totalSeats;
    }

    public long getConfirmedSeats() {
        return confirmedSeats;
    }

    public long getAvailableSeats() {
        return availableSeats;
    }

    public long getTotalUsers() {
        return totalUsers;
    }
}
