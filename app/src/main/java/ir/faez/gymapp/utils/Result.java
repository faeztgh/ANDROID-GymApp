package ir.faez.gymapp.utils;

import java.util.List;


public class Result<T> {

    private T item;
    private List<T> items;
    private Error error;

    public Result(T item, List<T> items, Error error) {
        this.item = item;
        this.items = items;
        this.error = error;
    }

    @Override
    public String toString() {
        String desc = "";
        if (error != null) {
            desc += "Error: " + error.getMessage();
            return desc;
        } else if (item != null) {
            desc += "Item: " + item.toString();
        } else if (items != null) {
            desc += "Items: ";
            for (T t : items) {
                desc += "\n" + t.toString();
            }
        }
        return desc;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
