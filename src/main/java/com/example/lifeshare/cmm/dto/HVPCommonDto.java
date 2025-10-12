package com.example.lifeshare.cmm.dto;

import com.google.common.base.CaseFormat;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @className : HVPCommonDto
 * @description :
 * @date : 2022-07-05 오후 15:20
 * @author : hanyk
 * @version : 1.0.0
 * @see
 * @history :
 **/
@Data
public class HVPCommonDto {
    private String orderBy;
    private String asc;
    private Integer limit;
    private Integer offset;

    public void updateOrderBy() {
        if (StringUtils.hasText(getOrderBy())) {
            String[] orderBySplits = getOrderBy().trim().split(",");

            for (int i = 0; i < orderBySplits.length; i++) {
                String[] splits = orderBySplits[i].trim().split("\\s+");
                splits[0] = "\"" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, splits[0]) + "\"";
                orderBySplits[i] = String.join(" ", splits);
            }

            setOrderBy(String.join(",", orderBySplits));
        }
    }

    public void updateOrderBy(Class<?> clz) {
        if (StringUtils.hasText(getOrderBy()) && clz != null) {
            String[] orderBySplits = getOrderBy().trim().split(",");
            Field[] fields = clz.getDeclaredFields();
            List<String> stringColumns = new ArrayList<>();

            for (Field field : fields) {
                if (field.getType() == String.class || field.getType() == char.class) {
                    stringColumns.add(field.getName());
                }
            }

            for (int i = 0; i < orderBySplits.length; i++) {
                String[] splits = orderBySplits[i].trim().split("\\s+");
                String upperUnderscoreName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, splits[0]);

                splits[0] = "" + upperUnderscoreName + "";
                orderBySplits[i] = String.join(" ", splits);
            }

            setOrderBy(String.join(",", orderBySplits));
        }
    }
}