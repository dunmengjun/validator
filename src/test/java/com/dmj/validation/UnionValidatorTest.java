package com.dmj.validation;

import static com.dmj.validation.config.GlobalConfig.getMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dmj.validation.ValidationResult.FieldResult;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.bean.InnerBean;
import com.dmj.validation.bean.UnionBean;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotNull;
import com.dmj.validation.constraint.Pattern;
import com.dmj.validation.constraint.Size;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.validator.union.AnyMatch;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UnionValidatorTest {

  @Test
  void should_return_ok_when_validate_given_default_group_and_valid_bean() {
    UnionBean unionBean = new UnionBean();
    unionBean.setAge(12);
    unionBean.setType("type");
    unionBean.setInnerBean(new InnerBean("xx"));

    ValidationResult actual = BeanValidator.validate(unionBean);

    assertEquals(ValidationResult.ok(), actual);
  }

  @Test
  void should_return_error_when_validate_given_default_group_and_invalid_bean() {
    UnionBean unionBean = new UnionBean();
    unionBean.setAge(12);
    unionBean.setType("type");

    ValidationResult actual = BeanValidator.validate(unionBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from(
            "innerBean.innerName", getMessage(NotBlank.class)
        )
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_two_error_when_validate_given_default_group_and_invalid_bean() {
    UnionBean unionBean = new UnionBean();

    ValidationResult actual = BeanValidator.validate(unionBean);

    List<UnionResult> expected = Lists.of(
        UnionResult.from(
            "Can't all be empty",
            Lists.of(
                FieldResult.from("age", getMessage(NotNull.class)),
                FieldResult.from("type", getMessage(NotBlank.class)),
                FieldResult.from("innerBean.innerName", getMessage(NotBlank.class))
            )
        ),
        UnionResult.from("age", getMessage(NotNull.class))
    );
    assertTrue(Lists.isEqualCollection(expected, actual.getResults()));
  }

  @Test
  void should_return_tree_error_when_validate_given_any_match_group() {
    UnionBean unionBean = new UnionBean();
    unionBean.setThirdName("");

    ValidationResult actual = BeanValidator.validate(unionBean, AnyMatch.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from(
            FieldResult.from("thirdName", getMessage(NotBlank.class)),
            FieldResult.from("thirdName", getMessage(Size.class, 2, 4)),
            FieldResult.from("thirdName", getMessage(Pattern.class, "\\d+"))
        )
    );
    assertEquals(expected, actual);
  }

}
