import org.junit.Assert;
import org.junit.Test;

public class TestAllWorkClassTest {
    @Test
    public void testSingleMethod() {
        Assert.assertEquals(5, TestAllWorkClass.testIt());
    }
}
