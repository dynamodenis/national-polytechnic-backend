package com.mabawa.nnpdairy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mabawa.nnpdairy.models.mongo.PImages;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String name;
    private String description;
    private Integer sel;
    private UUID category;
    private Integer type;
    private double avwcost;
    private Integer del;
    private Integer frezze;
    private String image_url;
    private String levy_1;
    private double pack_1;
    private double price_1;
    private UUID supplier;
    private String units_1;
    private double vat_perc;
    private double weight;
    private Integer service;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Nairobi")
    private Timestamp created;

    @Transient
    private List<PImages> pImages;

    @Transient
    private String pImage;


    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSel() {
        return sel;
    }
    public void setSel(Integer sel) {
        this.sel = sel;
    }

    public UUID getCategory() {
        return category;
    }
    public void setCategory(UUID category) {
        this.category = category;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public double getAvwcost() {
        return avwcost;
    }
    public void setAvwcost(double avwcost) {
        this.avwcost = avwcost;
    }

    public Integer getDel() {
        return del;
    }
    public void setDel(Integer del) {
        this.del = del;
    }

    public Integer getFrezze() {
        return frezze;
    }
    public void setFrezze(Integer frezze) {
        this.frezze = frezze;
    }

    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLevy_1() {
        return levy_1;
    }
    public void setLevy_1(String levy_1) {
        this.levy_1 = levy_1;
    }

    public double getPack_1() {
        return pack_1;
    }
    public void setPack_1(double pack_1) {
        this.pack_1 = pack_1;
    }

    public double getPrice_1() {
        return price_1;
    }
    public void setPrice_1(double price_1) {
        this.price_1 = price_1;
    }

    public UUID getSupplier() {
        return supplier;
    }
    public void setSupplier(UUID supplier) {
        this.supplier = supplier;
    }

    public String getUnits_1() {
        return units_1;
    }
    public void setUnits_1(String units_1) {
        this.units_1 = units_1;
    }

    public double getVat_perc() {
        return vat_perc;
    }
    public void setVat_perc(double vat_perc) {
        this.vat_perc = vat_perc;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Integer getService() {
        return service;
    }
    public void setService(Integer service) {
        this.service = service;
    }

    public Timestamp getCreated() {
        return created;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<PImages> getpImages() {
        return pImages;
    }
    public void setpImages(List<PImages> pImages) {
        this.pImages = pImages;
    }

    public String getpImage() {
        return pImage;
    }
    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}
