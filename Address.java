public class Address {
    private int streetNum;
    private int cityCode;

    // Constructor and getters/setters for street and city
     public Address(){
        streetNum = 0;
        cityCode = 0;
     }
    public Address(int streetNum, int cityCode) {
        this.streetNum = streetNum;
        this.cityCode = cityCode;
    }

    public int getStreetNum() {
        return streetNum;
    }

    public int getCityCode() {
        return cityCode;
    }
}
