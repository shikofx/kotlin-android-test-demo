package by.pda.demoapp.android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "Product")
public class ProductModel {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    double price;
    int rating;
    int colors;
    String desc;
    String currency;
    byte[] image;
    int imageVal;
    @TypeConverters(ColorModelConverters.class)
    List<ColorModel> colorList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getColors() {
        return colors;
    }

    public void setColors(int colors) {
        this.colors = colors;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getImageVal() {
        return imageVal;
    }

    public void setImageVal(int imageVal) {
        this.imageVal = imageVal;
    }

    public List<ColorModel> getColorList() {
        return colorList;
    }

    public void setColorList(List<ColorModel> colorList) {
        this.colorList = colorList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductModel that = (ProductModel) o;
        return id == that.id && Double.compare(price, that.price) == 0 && rating == that.rating && colors == that.colors && imageVal == that.imageVal && Objects.equals(title, that.title) && Objects.equals(desc, that.desc) && Objects.equals(currency, that.currency) && Objects.deepEquals(image, that.image) && Objects.equals(colorList, that.colorList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, rating, colors, desc, currency, Arrays.hashCode(image), imageVal, colorList);
    }
}
