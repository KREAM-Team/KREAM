package com.kream.kream.mappers;

import com.kream.kream.entities.CategoryDetailEntity;
import com.kream.kream.entities.ImageEntity;
import com.kream.kream.entities.ProductEntity;
import com.kream.kream.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

    UserEntity[] selectUser();

    UserEntity[] selectUserBySearch(@Param("filter") String filter,
                                    @Param("keyword") String keyword);

    int insertProduct(ProductEntity product);

    CategoryDetailEntity selectByCategoryId(String categoryDetailType);

    int insertImage(ImageEntity image);

    ImageEntity selectImageByProductIdAndIsPrimary(Integer id);

    ProductEntity[] selectProduct();

    ProductEntity selectProductById(Integer id);

    ProductEntity[] selectProductBySearch(@Param("filter") String filter,
                                          @Param("keyword") String keyword);

    int updateProduct(@Param("product") ProductEntity product);
}
