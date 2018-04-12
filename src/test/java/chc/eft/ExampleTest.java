package chc.eft;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by cletus on 2018/3/27.
 */


public class ExampleTest {

    Example example = new Example();

    @Test
    public void homeMessageShouldBeFun() {
        assertThat(example.home(), is("Hello World! ECS + ECR"));
    }
}
