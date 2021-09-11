package Contants;

public enum EndPoints {
    DEVICES( "http://localhost:3000/devices");
    String url;

    EndPoints(String url){
        this.url = url;
    }
    @Override
    public String toString() {
        return this.url;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUrl(Long id) {
        return this.url+"/"+id;
    }
}
