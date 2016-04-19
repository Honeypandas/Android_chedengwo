package nearbysite;

/**
 * Created by Administrator on 2016/4/7.
 */
public class nearbysites {
    private int id;
    private String name;
    private String busid;
    private String distance;

    public nearbysites() {

    }

    public nearbysites(int id, String name, String busid, String distance) {
        this.id = id;
        this.name = name;
        this.busid = busid;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBusid() {
        return busid;
    }

    public String getDistance() {
        return distance;
    }


}
