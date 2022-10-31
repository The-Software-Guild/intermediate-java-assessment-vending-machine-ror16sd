package dao;

import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dao.VendingMachineDaoFileImpl;
import com.sg.vendingmachine.dto.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class VendingMachineDaoFileImplTest {
    public static VendingMachineDao testDao;
    public VendingMachineDaoFileImplTest(){

    }

    @BeforeAll
    public static void setUpClass() throws Exception {
        String testFile = "testproduct.txt";
        new FileWriter(testFile);
        testDao = new VendingMachineDaoFileImpl(testFile);
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        testDao = new VendingMachineDaoFileImpl("testproduct.txt");
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @org.junit.jupiter.api.Test
    public void testGetProduct() throws Exception{
        String productName = "Test";
        BigDecimal productCost = new BigDecimal(4.25).setScale(2, RoundingMode.FLOOR);
        int productStock = 5;
        Product testProduct = new Product(productName,productCost, productStock);
        productName = "GET_PRODUCT_SetTestName";
        testProduct.setName(productName);
        testProduct.setCost(new BigDecimal(9.33).setScale(2, RoundingMode.FLOOR));
        testProduct.setNumInStock(933);
        //create product and add to product list
        testDao.addItem(testProduct);
        Product retrievedProduct = testDao.getItem(productName);

        //check if retrieved object matches the one created
        assertNotNull(retrievedProduct, "Object is not null");
        assertEquals(testProduct.getName(),
                    retrievedProduct.getName(),
                    "Checking Product Name");
        assertEquals(testProduct.getCost(),
                    retrievedProduct.getCost(),
                    "Checking Product Cost");
        assertEquals(testProduct.getNumInStock(),
                    retrievedProduct.getNumInStock(),
                    "Checking Product Quantity In Stock");

        //precaution, remove item from list
        testDao.removeItem(retrievedProduct);
    }

    @org.junit.jupiter.api.Test
    public void testListAllProducts() throws Exception{
        String productName1 = "Test1";
        BigDecimal productCost1 = new BigDecimal(1.01).setScale(2, RoundingMode.FLOOR);
        int productStock1 = 10;
        Product testProduct = new Product(productName1,productCost1, productStock1);
        testProduct.setName("SetTestName1");
        productName1 = "SetTestName1";
        testProduct.setCost(new BigDecimal(1.10).setScale(2, RoundingMode.FLOOR));
        testProduct.setNumInStock(11);
        testDao.addItem(testProduct);
        Product retrievedDaoProduct1 = testDao.getItem(productName1);

        String productName2 = "Test2";
        BigDecimal productCost2 = new BigDecimal(2.25).setScale(2, RoundingMode.FLOOR);
        int productStock2 = 2;
        Product testProduct2 = new Product(productName2,productCost2, productStock2);
        testProduct2.setName("SetTestName2");
        productName2 = "SetTestName2";
        testProduct2.setCost(new BigDecimal(2.22).setScale(2, RoundingMode.FLOOR));
        testProduct2.setNumInStock(22);
        testDao.addItem(testProduct2);
        Product retrievedDaoProduct2 = testDao.getItem(productName2);

        //testing for 0 values to be maintained in dao list
        String productName3 = "Test3";
        BigDecimal productCost3 = new BigDecimal(3.33).setScale(2, RoundingMode.FLOOR);
        int productStock3 = 3;
        Product testProduct3 = new Product(productName3,productCost3, productStock3);
        testProduct3.setName("SetTestName3");
        productName3 = "SetTestName3";
        testProduct3.setCost(new BigDecimal(3.33).setScale(2, RoundingMode.FLOOR));
        testProduct3.setNumInStock(0);
        testDao.addItem(testProduct3);
        Product retrievedDaoProduct3 = testDao.getItem(productName3);

        //created three Product objects to be tested
        //get list of all added Products w/n the DAO
        List<Product> testList = testDao.listAllItems();

        assertNotNull(testList, "The list of Products is not null");
        assertEquals(3, testList.size(), "List of Products should be 3");
        assertTrue(testDao.listAllItems().contains(retrievedDaoProduct1),
                "The list should contain Product - SetTestName1");
        assertTrue(testDao.listAllItems().contains(retrievedDaoProduct2),
                "The list should contain Product - SetTestName2");
        assertTrue(testDao.listAllItems().contains(retrievedDaoProduct3),
                "The list should contain Product - SetTestName3");

        testDao.removeItem(testList.remove(2));
        testDao.removeItem(testList.remove(1));
        testDao.removeItem(testList.remove(0));

        assertEquals(0,testList.size(), "List is empty");
    }

    @org.junit.jupiter.api.Test
    public void testChangeInventory() throws Exception{
        String productName3 = "Candy";
        BigDecimal productCost3 = new BigDecimal(2.00).setScale(2, RoundingMode.FLOOR);
        int productStock3 = 3;
        Product testProduct3 = new Product(productName3,productCost3, productStock3);
        testProduct3.setName("SetTestName3");
        testProduct3.setCost(new BigDecimal(1.25).setScale(2, RoundingMode.FLOOR));
        testProduct3.setNumInStock(4);
        testDao.addItem(testProduct3);

        List<Product> testList = testDao.listAllItems();

        //testing changeInventoryCount only by 1 since only one item to be sold at a time
        Product changeStockProduct = testDao.changeInventoryCount(testProduct3,1);
        assertNotNull(testDao.listAllItems());
        assertEquals(1,testList.size(), "There is only 1 product in the list");
        assertNotEquals(testProduct3.getNumInStock(), changeStockProduct.getNumInStock(),
                "testProduct3 numInStock is 4, changedStockProduct numInStock is 3. They are not equal");

        testDao.removeItem(testProduct3);
    }

}
