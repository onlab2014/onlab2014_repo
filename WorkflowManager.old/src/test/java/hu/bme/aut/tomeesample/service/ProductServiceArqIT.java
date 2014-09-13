package hu.bme.aut.tomeesample.service;

//import hu.bme.aut.tomeesample.model.Product;
//
//@RunWith(Arquillian.class)
//public class ProductManagerArqIT {
//	
//	@Inject
//	private ProductService productService;
//	
//	@PersistenceContext
//	EntityManager em;
//	
//	@Inject
//	UserTransaction tx;
//	
//	@Deployment
//	public static WebArchive createArchiveAndDeploy(){
//		WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
//				.addClasses(ProductService.class, Product.class)
//				.addAsResource("arq-persistence.xml", "META-INF/persistence.xml")
//				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
//				.addAsWebInfResource("resources.xml", "resources.xml");
//		
//		System.out.println(webArchive.toString(true));
//		return webArchive;
//	}
//	
//	
//	@Before
//	public void setup(){
//		
//		try {
//			tx.begin();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		em.createQuery("DELETE FROM Product").executeUpdate();
//		em.createQuery("DELETE FROM Category").executeUpdate();		
//		//kezdeti adatok beszúrhatók
//		
//		try {
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//	}
//	
//	@Test
//	public void addProductIntoExistingCategory(){
//		
//		Product newProduct = new Product("NewProd", 60.0);
//		productService.createProduct(newProduct);
//		
//		List<Product> products = productService.em.createQuery("SELECT p FROM Product p").getResultList();
//		assertEquals(1, products.size());
//		Product foundProduct = products.get(0);
//		assertEquals(newProduct.getName(), foundProduct.getName());
//		assertEquals(newProduct.getPrice(), foundProduct.getPrice(), 0.00001);
//		
//		
//	}
//	
//	
//	@After
//	public void destroy(){
//		
//	}
//	
//}
