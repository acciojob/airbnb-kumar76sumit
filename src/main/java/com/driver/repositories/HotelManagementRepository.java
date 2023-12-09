package com.driver.repositories;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HotelManagementRepository {

    private HashMap<String,Hotel> hotelDb;
    private HashMap<Integer,User> userDb;
    private HashMap<String,Booking> bookingDb;

    public HotelManagementRepository() {
        hotelDb=new HashMap<>();
        userDb=new HashMap<>();
        bookingDb=new HashMap<>();
    }

    public String addHotel(Hotel hotel){

        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.
        if(hotel==null || hotel.getHotelName()==null || hotelDb.containsKey(hotel.getHotelName()))
        {
            return "FAILURE";
        }
        hotelDb.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user){

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        if(!userDb.containsKey(user.getaadharCardNo()))
        {
            userDb.put(user.getaadharCardNo(),user);
        }
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities(){

        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        int maxFacilities=0;
        String currHotelName="";
        for(String hotelName:hotelDb.keySet())
        {
            Hotel hotel=hotelDb.get(hotelName);
            if(hotel.getFacilities().size()>=maxFacilities)
            {
                if(currHotelName=="")
                {
                    currHotelName=hotelName;
                }
                else if(hotel.getFacilities().size()>maxFacilities || currHotelName.compareTo(hotelName)>0)
                {
                    currHotelName=hotelName;
                }
                maxFacilities=hotel.getFacilities().size();
            }
        }
        if(maxFacilities==0) return "";
        return currHotelName;
    }

    public int bookARoom(Booking booking){

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid

        String hotelName=booking.getHotelName();
        if(hotelDb.get(hotelName).getAvailableRooms()<booking.getNoOfRooms())
        {
            return -1;
        }
        UUID bookingId=UUID.randomUUID();
        booking.setBookingId(bookingId.toString());
        booking.setAmountToBePaid(booking.getNoOfRooms()*hotelDb.get(hotelName).getPricePerNight());
        bookingDb.put(bookingId.toString(),booking);
        return booking.getAmountToBePaid();
    }

    public int getBookings(Integer aadharCard)
    {
        //In this function return the bookings done by a person
        int totalBookings=0;
        for(String bookingId:bookingDb.keySet())
        {
            if(bookingDb.get(bookingId).getBookingAadharCard()==aadharCard)
            {
                totalBookings++;
            }
        }
        return totalBookings;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        Hotel hotel=hotelDb.get(hotelName);
        List<Facility> extraFacilities=new ArrayList<>();
        for(Facility facility:newFacilities)
        {
            if(!hotel.getFacilities().contains(facility))
            {
                extraFacilities.add(facility);
            }
        }
        hotel.setFacilities(extraFacilities);
        return hotel;
    }
}
