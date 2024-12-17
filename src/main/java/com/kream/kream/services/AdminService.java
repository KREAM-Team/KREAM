package com.kream.kream.services;

import com.kream.kream.entities.CategoryDetailEntity;
import com.kream.kream.entities.ImageEntity;
import com.kream.kream.entities.ProductEntity;
import com.kream.kream.entities.UserEntity;
import com.kream.kream.exceptions.TransactionalException;
import com.kream.kream.mappers.AdminMapper;
import com.kream.kream.results.CommonResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AdminService {
    private final AdminMapper adminMapper;

    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public UserEntity[] selectUser() {
        return this.adminMapper.selectUser();
    }

    public UserEntity[] searchUser(String filter, String keyword) {
        if (filter == null || !filter.equals("all") && !filter.equals("email") && !filter.equals("nickName") && !filter.equals("suspended")) {
            filter = "all";
        }
        if (keyword == null) {
            keyword = "";
        }
        return this.adminMapper.selectUserBySearch(filter, keyword);
    }

    public ProductEntity[] selectProduct() {
        return this.adminMapper.selectProduct();
    }

    public ProductEntity[] searchProduct(String filter, String keyword) {
        if (filter == null || !filter.equals("all") && !filter.equals("modelNum") && !filter.equals("name") && !filter.equals("brand") && !filter.equals("category")) {
            filter = "all";
        }
        if (keyword == null) {
            keyword = "";
        }
        return this.adminMapper.selectProductBySearch(filter, keyword);
    }

    public ProductEntity getProductById(Integer id) {
        if (id == null || id < 1) {
            return null;
        }
        return this.adminMapper.selectProductById(id);
    }

    public ImageEntity selectImageByProductId(Integer id) {
        if (id == null || id < 1) {
            return null;
        }
        return this.adminMapper.selectImageByProductIdAndIsPrimary(id);
    }

    @Transactional
    public CommonResult deleteProducts(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return CommonResult.FAILURE;
        }
        for (int id : ids) {
            ProductEntity product = this.adminMapper.selectProductById(id);
            if (product == null || product.isDeleted()) {
                throw new TransactionalException();
            }
            product.setDeleted(true);
            if (this.adminMapper.updateProduct(product) == 0) {
                throw new TransactionalException();
            }
        }
        return CommonResult.SUCCESS;
    }

    public CategoryDetailEntity findByCategoryId(String categoryDetailType) {
        if (categoryDetailType == null) {
            return null;
        }
        return this.adminMapper.selectByCategoryId(categoryDetailType);
    };

    // UserEntity user 넣어서 관리자 아닐시 null 조건 적어야함.
    public CommonResult addProduct(ProductEntity product, CategoryDetailEntity categoryDetail, MultipartFile[] files) throws IOException {
//        if (user == null || user.isAdmin == false || user.isSuspended() || user.getDeletedAt() != null) {
//            return CommonResult.FAILURE_UNSIGNED;
//        }
        if (product == null ||
                product.getModelNumber() == null || product.getModelNumber().isEmpty() || product.getModelNumber().length() > 50 ||
                product.getProductNameKo() == null || product.getProductNameKo().isEmpty() || product.getProductNameKo().length() > 100 ||
                product.getProductNameEn() == null || product.getProductNameEn().isEmpty() || product.getProductNameEn().length() > 100 || product.getGender() == null || product.getCategory() == null || product.getColor() == null) {
            return CommonResult.FAILURE;
        }
        if (categoryDetail == null) {
            return CommonResult.FAILURE;
        }
        product.setCategoryDetailId(categoryDetail.getId());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(null);
        product.setDeleted(false);

        this.adminMapper.insertProduct(product);

        if (files == null || files.length == 0) {
            return CommonResult.FAILURE;

        } else {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                ImageEntity image = new ImageEntity();
                image.setProductId(product.getId());
                image.setData(file.getBytes()); // 바이 배열로 벼화는 중 예외 발생 가능
                image.setType(file.getContentType());
                image.setName(file.getOriginalFilename());
                image.setCreatedAt(LocalDateTime.now());

                if (i == 0) {
                    image.setPrimary(true);
                } else {
                    image.setPrimary(false);
                }
                this.adminMapper.insertImage(image);
            }
        }
        return CommonResult.SUCCESS;
    };
}
