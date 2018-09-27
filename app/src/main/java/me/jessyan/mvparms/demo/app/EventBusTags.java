/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.mvparms.demo.app;

import org.simple.eventbus.EventBus;

/**
 * ================================================
 * 放置 {@link EventBus} 的 Tag ,便于检索
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.5">EventBusTags wiki 官方文档</a>
 * Created by JessYan on 8/30/2016 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface EventBusTags {

    /**
     * 城市切换
     */
    String CITY_CHANGE_EVENT = "city_change_tag";
    /**
     * 登录状态改变
     */
    String LOGIN_STATUS_CHANGE_EVENT = "login_status_change_Event";
    /**
     * 优惠卷改变
     */
    String CHANGE_COUPON = "change_coupon";
    /**
     * 套餐支付成功
     */
    String MEAL_PAY_SUCCESS = "meal_pay_success";

    /**
     * 修改预约时间
     */
    String CHANGE_APPOINTMENT_TIME = "change_appointment_time";

    /**
     * 更新用户信息
     */
    String USER_INFO_CHANGE = "user_info_change";

    /**
     * 更新用户账户信息
     */
    String USER_ACCOUNT_CHANGE = "user_account_change";

    /**
     * 更新用户基本信息
     */
    String USER_BASE_INFO_CHANGE = "user_base_info_change";

    /**
     * 退出登录
     */
    String USER_LOGOUT = "user_logout";

    /**
     * 发表日记，切换项目
     */
    String CHANGE_DIRAY_PROJECT = "change_diray_project";

    /**
     * 切换医院
     */
    String HOSPITAL_CHANGE_EVENT = "hospital_change_event";
    /**
     * 切换地址
     */
    String ADDRESS_CHANGE_EVENT = "address_change_event";
    /**
     * 切换店铺
     */
    String STORE_CHANGE_EVENT = "store_change_event";
    /**
     * 切换预约时间
     */
    String APPOINTMENTS_CHANGE_EVENT = "appointments_change_event";
    /**
     * 切换优惠卷
     */
    String COUPON_CHANGE_EVENT = "coupon_change_event";

    /**
     * 添加地址成功
     */
    String ADD_ADDRESS_SUCCESS = "add_address_success";
    /**
     * 更改首页
     */
    String CHANGE_MAIN_ITEM = "change_main_item";
    /**
     * 评论日记
     */
    String DIARY_COMMENT_SUCCESS = "change_main_item";


}
