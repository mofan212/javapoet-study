package indi.mofan.tests;

import com.squareup.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author mofan
 * @date 2024/1/28 17:32
 */
public class IndentationTest {
    @Test
    public void testCodeBlock() {
        CodeBlock block = CodeBlock.builder()
                .indent()
                .add("\n")
                .addStatement("int a = 1")
                .addStatement("int b = a + 1")
                .unindent()
                .build();
        // 移除第一行空行
        String result = StringUtils.substringAfter(block.toString(), "\n");
        String expectStr = """
                  int a = 1;
                  int b = a + 1;
                """;
        assertThat(result).isEqualTo(expectStr);
    }
}
