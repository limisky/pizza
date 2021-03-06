package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Administrator
 */
public class PizzaBean {
    private String url;
    private int id;
    private String name;
    private double price;
    private String description;
    private String pic_url;
    private int sales;
    
    private Connection con;
    
    public PizzaBean(){
    }
    public PizzaBean(String _url){
        url=_url;
    }
    public PizzaBean(String _url, int _idproduct){
        url = _url;
        id = _idproduct;
    }
    public void addCom(Integer idPizza, Integer idCom,Integer quantity)throws SQLException{
        String sql = "INSERT INTO `product_component` (`idproduct`, `idcomponent`, `quantity`) ";
        sql += "VALUES (?,?,?)";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,idPizza);
            pstmt.setInt(2,idCom);
            pstmt.setInt(3,quantity);
            pstmt.execute();
            con.commit();           
        }
        catch(Exception e){
            con.rollback();
        }
    }
    public Integer addPizza() throws SQLException{
        Integer idpizza=0;
        String addPizzaSQL = "INSERT INTO `product` (`name`, `price`, `description`, `pic_url`) ";
        addPizzaSQL += "VALUES (?,?,?,?)";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(addPizzaSQL);
            pstmt.setString(1,name);
            pstmt.setDouble(2,price);
            pstmt.setString(3,description);
            pstmt.setString(4,pic_url);
            pstmt.execute();
            con.commit();
            
            String sql = "SELECT idproduct from `product`";
            Statement stmt = con.createStatement();
            ResultSet rs= stmt.executeQuery(sql);
            while(rs.next()){
                idpizza = rs.getInt("idproduct");   
            }         
        }
        catch(Exception e){
            con.rollback();
        }
        return idpizza;
    }
    public boolean checkStock(Integer quantity)throws Exception{
        try{    
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
            
            String querySQL = "SELECT * from `product_component`,`component` where product_component.idcomponent=component.idcomponent"
                    + " and idproduct ='"+id+"'";
            Statement stmt = con.createStatement();
            ResultSet rs= stmt.executeQuery(querySQL);
            
            while(rs.next()){
                if((rs.getInt("quantity")*quantity)>(rs.getInt("stock")))
                    return false;
            }
            rs= stmt.executeQuery(querySQL);
            while(rs.next()){
                int newstock = rs.getInt("stock")-(rs.getInt("quantity")*quantity);
                String sql = "UPDATE `component` SET `stock`="+newstock+" WHERE `idcomponent`='"+rs.getInt("idcomponent")+"';";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.execute();
                con.commit(); 
            }
        
        }
        catch(Exception e){
            con.rollback();
        }
        return true;
    }
    public void addSales(int quantity)throws Exception{
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
            
            String querySQL = "SELECT sales from `product` where idproduct ='"+id+"'";
            Statement stmt = con.createStatement();
            ResultSet rs= stmt.executeQuery(querySQL);
            rs.next();
            quantity += rs.getInt("sales");  
            
            String sql = "UPDATE `product` SET `sales`="+quantity+" WHERE `idproduct`='"+id+"';";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.commit();         
        }
        catch(Exception e){
            con.rollback();
        }
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the pic_url
     */
    public String getPic_url() {
        return pic_url;
    }

    /**
     * @param pic_url the pic_url to set
     */
    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    /**
     * @return the sals
     */
    public int getSales() {
        return sales;
    }

    /**
     * @param sals the sals to set
     */
    public void setSales(int sales) {
        this.sales = sales;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
