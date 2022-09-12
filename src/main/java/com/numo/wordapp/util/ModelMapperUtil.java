package com.numo.wordapp.util;

import com.numo.wordapp.model.Word;
import org.modelmapper.ModelMapper;

/****
* @Name: ModelMapperUtil
* @Author: 정현경
* @Date: 2022-09-11
* @Description: Entity <-> DTO 변환 클래스
****/

public class ModelMapperUtil {
    private ModelMapper modelMapper;

    public ModelMapperUtil(){
        this.modelMapper = new ModelMapper();
    }

    /*
     * @Name: getModelMapper
     * @Author: 정현경
     * @Date: 2022-09-11
     * @Description: 싱글톤 패턴 적용
     */
    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    /*
     * @Name: of
     * @Param:
     *  - obj: 변환할 타입
     *  - cls: 변환할 클래스
     * @returnValue: 지정한 타입의 객체
     * @Author: 정현경
     * @Date: 2022-09-11
     * @Description:
     */
    public <T> T of(Object obj, Class<T> cls){
        T entity = this.modelMapper.map(obj, cls);
        return entity;
    }

}
