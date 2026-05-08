package data;

public class CourierData {

    public static final String BASE_API_URL= "https://qa-scooter.praktikum-services.ru";

    public static final String COURIER_CREATE_ENDPOINT = "/api/v1/courier";
    public static final String COURIER_LOGIN_ENDPOINT = "/api/v1/courier/login";
    public static final String ORDER_CREATE_ENDPOINT = "/api/v1/orders";
    public static final String COURIER_DELETE_ENDPOINT = "/api/v1/courier/{id}";
    public static final String ORDER_CANCEL_ENDPOINT = "/api/v1/orders/cancel";
    public static final String ORDER_LIST_ENDPOINT = "/api/v1/orders/track";

    public static final String COURIER_LOGIN = "Zina" +System.currentTimeMillis();
    public static final String COURIER_GUEST_LOGIN = "Zino";
    public static final String COURIER_PASSWORD = "1234";
    public static final String COURIER_GUEST_PASSWORD = "4568";
    public static final String COURIER_FIRST_NAME = "Ivanova";
    public static final String COURIER_GUEST_FIRST_NAME = "Petrova";
    public static final String EMPTY_LOGIN = "";
    public static final String EMPTY_PASSWORD = "";

}
