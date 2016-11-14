public class kNNTest {
	double[][] accelerometer = {{1.4, 4334.56, 1235.62}, {1235.324, 124.5242, 414.4}, {123.23, 1235.32, 125.32}};
	double[][] gyro = {{1, 2.56, 1235.62}, {129.324, 0.5242, 414.4}, {123.23, 4235.32, 1025.32}};
	
	public void testAssertions() throws Exception {
		kNNClassifier cls = new kNNClassifier(accelerometer, gyro);
		String result = cls.classifyInstance();
		System.out.println(result);
	}
	
	public static void main(String args[]) {
		kNNTest test = new kNNTest();
		try {
			test.testAssertions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
