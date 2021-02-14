package ir.faez.gymapp.data.model;

public class Video {

    public String _type;
    public String name;
    public String url;
    public String type;

    public Video(String _type, String name, String url, String type) {
        this._type = _type;
        this.name = name;
        this.url = url;
        this.type = type;
    }


    public String get_type() {
        return _type;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }
}