package service;

import com.sg.vendingmachine.dao.VendingMachineAuditDaoImpl;
import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dao.VendingMachineDaoFileImpl;
import com.sg.vendingmachine.dto.Product;
import com.sg.vendingmachine.service.*;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VendingMachineServiceLayerImplTest {

    public static VendingMachineServiceLayer service;
    public static VendingMachineServiceLayer testdao;

    public VendingMachineServiceLayerImplTest() throws Exception{
   }

    @BeforeAll
    public static void setUpClass() throws Exception, VendingMachinePersistenceException{
        String testFile = "testserviceproduct.txt";
        new FileWriter(testFile);
        service = new VendingMachineServiceLayerImpl(new VendingMachineDaoFileImpl(testFile), new VendingMachineAuditDaoImpl());
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws VendingMachinePersistenceException {
        service = new VendingMachineServiceLayerImpl(new VendingMachineDaoFileImpl("testserviceproduct.txt"), new VendingMachineAuditDaoImpl());
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetItem() throws Exception {
        String productName = "Test";
        BigDecimal productCost = new BigDecimal(4.25).setScale(2, RoundingMode.FLOOR);
        int productStock = 5;
        Product testProduct = new Product(productName,productCost, productStock);
        testProduct.setName("SetTestName");
        testProduct.setCost(new BigDecimal(9.33).setScale(2, RoundingMode.FLOOR));
        testProduct.setNumInStock(933);
        service.addProduct(testProduct);
        Product retrievedProduct = service.getProduct(testProduct.getName());

        assertEquals(testProduct.getName(),
                retrievedProduct.getName(),
                "Checking Product Name");
        assertEquals(testProduct.getCost(),
                retrievedProduct.getCost(),
                "Checking Product Cost");
        assertEquals(testProduct.getNumInStock(),
                retrievedProduct.getNumInStock(),
                "Checking Product Quantity In Stock");
        service.removeProduct(retrievedProduct);

    }

        /*
         * Test of listAllItems method, of class VendingMachineServiceImpl.
         */

    @Test
    public void testListAllItems() throws Exception {
        String productName1 = "ListTest1";
        BigDecimal productCost1 = new BigDecimal(1.01).setScale(2, RoundingMode.FLOOR);
        int productStock1 = 10;
        Product listTestProduct = new Product(productName1,productCost1, productStock1);
        listTestProduct.setName("LISTALL_SetTestName1");
        listTestProduct.setCost(new BigDecimal(1.09).setScale(2, RoundingMode.FLOOR));
        listTestProduct.setNumInStock(19);
        service.addProduct(listTestProduct);

        String productName2 = "ListTest2";
        BigDecimal productCost2 = new BigDecimal(2.25).setScale(2, RoundingMode.FLOOR);
        int productStock2 = 2;
        Product listTestProduct2 = new Product(productName2,productCost2, productStock2);
        listTestProduct2.setName("LISTALL_SetTestName2");
        listTestProduct2.setCost(new BigDecimal(2.29).setScale(2, RoundingMode.FLOOR));
        listTestProduct2.setNumInStock(29);
        service.addProduct(listTestProduct2);

        //testing for 0 values to be maintained in service list
        String productName3 = "ListTest3";
        BigDecimal productCost3 = new BigDecimal(3.33).setScale(2, RoundingMode.FLOOR);
        int productStock3 = 3;
        Product listTestProduct3 = new Product(productName3,productCost3, productStock3);
        listTestProduct3.setName("LISTALL_SetTestName3");
        listTestProduct3.setCost(new BigDecimal(3.33).setScale(2, RoundingMode.FLOOR));
        listTestProduct3.setNumInStock(0);
        service.addProduct(listTestProduct3);

        //get list of all added Products w/n the service layer
        List<Product> testList = service.listAllProducts();

        //only list 2 out of the 3 added to list, since one obj has 0 for numInStock
        assertNotNull(testList, "The list of Products is not null");
        assertEquals(2, testList.size(), "List of Products should be 2");
        assertTrue(service.listAllProducts().contains(listTestProduct),
                "The list should contain Product - LISTALL_SetTestName1");
        assertTrue(service.listAllProducts().contains(listTestProduct2),
                "The list should contain Product - LISTALL_SetTestName2");
        assertFalse(service.listAllProducts().contains(listTestProduct3),
                "The list should NOT contain Product - LISTALL_SetTestName3");

        service.removeProduct(listTestProduct);
        service.removeProduct(listTestProduct2);
        service.removeProduct(listTestProduct3);

    }

    /**
     * Test of changeInventoryCount method, of class VendingMachineServiceImpl.
     */
    @Test
    public void testChangeInventoryCount(){
        Product testProduct = new Product("Cheetos",  new BigDecimal(2.99).setScale(2, RoundingMode.FLOOR), 18);
        try{
            service.addProduct(testProduct);
            Product retrievedProduct = service.getProduct(testProduct.getName());
            service.changeInventoryCount(retrievedProduct, 10);
            assertNotNull(retrievedProduct, "Item should not be null");
            assertEquals(8, retrievedProduct.getNumInStock(), "Inventory item should be 8");
            service.removeProduct(retrievedProduct);
        } catch(VendingMachinePersistenceException | VendingMachineNoItemInventoryException |
                VendingMachineDataValidationException e){
            fail("No way it will go wrong");

    }
        }


    /*
     * Test of sellItem method, of class VendingMachineServiceImpl.
     */
    @Test
    public void testSellItem() throws Exception {
        Product testSellProduct = new Product("TestCandy",  new BigDecimal(2.99).setScale(2, RoundingMode.FLOOR), 2);
        service.addProduct(testSellProduct);
        Product retrievedProduct = service.getProduct(testSellProduct.getName());
        assertNotNull(service.listAllProducts(), "list is not null/empty");
        BigDecimal testFunds = new BigDecimal(3.00).setScale(2, RoundingMode.FLOOR);
        BigDecimal soldFunds = service.sellProduct(testFunds,retrievedProduct);
        assertEquals(soldFunds,new BigDecimal(0.01).setScale(2, RoundingMode.FLOOR),
                "Funds should equal 0.01");

        service.removeProduct(retrievedProduct);
    }
}
