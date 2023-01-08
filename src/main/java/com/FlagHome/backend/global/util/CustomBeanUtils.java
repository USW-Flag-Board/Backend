package com.FlagHome.backend.global.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

@Component
public class CustomBeanUtils<T> {

    // 제가 작성한 코드는 아니고 가져온 코드입니다!
    // 해석을 위한 주석을 추가했습니다 (22.01.07 강지은)
    public T copyNotNullProperties(T source, T destination) {
        if (source == null || destination == null || source.getClass() != destination.getClass())
            return null;

        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper dest = new BeanWrapperImpl(destination);

        // 수정하려는 클래스의 Field[]를 배열로 가져옴,
        // Patch시 필드의 데이터가 null이 아니면 수정되는 값이기 때문에
        // null체크 이후 수정 사항을 dest에 반영함.
        // 클래스에 대한 정보이기 때문에 destination을 리턴하는듯

        for(final Field property : source.getClass().getDeclaredFields()) {
            Object sourceProperty = src.getPropertyValue(property.getName());
            if(sourceProperty!= null && !(sourceProperty instanceof Collection<?>)) { // 뒤에 컬렉션 체크는 컬렉션을 수정하는 케이스가 없어서 막아놓은 것
                dest.setPropertyValue(property.getName(), sourceProperty);
            }
        }

        return destination;
    }

}
