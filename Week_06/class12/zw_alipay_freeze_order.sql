SET NAMES utf8mb4;

CREATE TABLE `zw_alipay_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `mobile_phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='买家信息表';

CREATE TABLE `zw_alipay_commodity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识ID',
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `status` int(1) NOT NULL COMMENT '商品状态 0 表示未上架 1表示上架  2表示删除',
  `original_price` float NOT NULL COMMENT '商品原价',
  `first_image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品首图',
  `modifier` bigint(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` bigint(20) DEFAULT '0' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `stage_price` float DEFAULT '0',
  `is_deleted` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='商品表';

CREATE TABLE `zw_alipay_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识ID',
  `order_number` varchar(22) DEFAULT NULL COMMENT '客户订单号码',
  `request_number` varchar(22) DEFAULT NULL COMMENT '支付订单号码',
  `customer_id` int(11) DEFAULT NULL COMMENT 'APP下单用户id',
  `order_amount` decimal(8,2) DEFAULT NULL COMMENT '订单金额',
  `order_address_id` varchar(255) DEFAULT NULL COMMENT '订单地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `transaction_time` timestamp NULL DEFAULT NULL COMMENT '交易时间',
  `trade_status` int(2) DEFAULT '0' COMMENT '交易状态 0:订单初始 1:试用中 2:已购买 3:取消预授权 4:申请还机 5:退货成功',
  `freeze_amount` decimal(8,2) DEFAULT NULL COMMENT '实付金额',
  `complete_time` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `pay_status` int(2) DEFAULT NULL COMMENT '订单支付状态 0失败,1成功',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `product_count` int(11) DEFAULT NULL COMMENT '产品数量',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `payment_way` int(2) DEFAULT '0' COMMENT '支付方式 0直接购买 1分期支付 2试用预授权',
  `is_deleted` int(2) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `union_order_number` (`order_number`) USING BTREE,
  KEY `union_request_number` (`request_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商城订单表';

SET FOREIGN_KEY_CHECKS = 1;
