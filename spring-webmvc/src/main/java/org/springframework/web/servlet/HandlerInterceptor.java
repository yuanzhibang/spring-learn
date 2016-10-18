/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

/**
 * Workflow接口允许定制handler执行链,
 * 程序可以注册一些自带的拦截器或定制的拦截器,拦截器可以拦截一组handler,
 * 并且在不修改这一组handler实现的前提下添加一些通用的行为。
 *
 * <p>在对应的HandlerAdapter触发handler之前,HandlerInterceptor会被调用。
 * 这种机制应用到很多预处理的场景,例如:权限校验,国际化,主题切换。
 * 最主要的目的是分解出handler的重复代码。
 *
 * <p>在一个异步处理场景里, 当主线程没有渲染model或者执行{@code postHandle}和{@code afterCompletion}回调时,
 * handler可能会在另一个独立线程中执行。当handler并发执行完毕,请求会转发回来执行渲染model的方法。
 * 详情: {@code org.springframework.web.servlet.AsyncHandlerInterceptor}
 *
 * <p>一般情况下,一个HandlerMapping bean对应一个拦截器链。
 * 拦截器就是一个bean,HandlerMapping bean通过interceptors属性引用拦截器bean。
 * XML配置中,interceptors可以是一个list,也可以是一个ref。
 *
 * <p>HandlerInterceptor很类似Servlet过滤器,
 * Servlet过滤器更加强大,比如可以通过交换request和response内的对象,提前结束过滤链。
 * 相比过Servlcet滤器,HandlerInterceptor仅允许定制预处理和后处理,通过选择性的禁用handler执行。
 * 注意:Servlet过滤器配置在web.xml里,HandlerInterceptor在Application Context里。
 *
 * <p> 基本的指导思想:抽象出细粒度的,基于handler的处理任务,HanlderInterceptor实现对这些任务进行处理,
 * 特别是要抽取出通用的handler代码和权限校验。
 * 另外,Servlet过滤器很适合处理request内容和视图内容,  比如,复合表单和GZIP压缩。
 *
 * @author Juergen Hoeller
 * @translator laoyuan
 * @since 20.06.2003
 * @see HandlerExecutionChain#getInterceptors
 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter
 * @see org.springframework.web.servlet.handler.AbstractHandlerMapping#setInterceptors
 * @see org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor
 * @see org.springframework.web.servlet.i18n.LocaleChangeInterceptor
 * @see org.springframework.web.servlet.theme.ThemeChangeInterceptor
 * @see javax.servlet.Filter
 */
public interface HandlerInterceptor {

	/**
	 * 拦截handler执行。
	 * 本方法执行时机:HandlerMapping已经确定了相应的handler对象之后,HandleAdapter调用Handler之前。
	 *
	 * <p>DispatcherServlet在执行链中处理handler,执行链由任意数量的拦截器和最终的handler组成。
     * 本方法可以打断执行链,例如发送一个HTTP错误或者直接向response写入内容。
	 *
	 * <p><strong>注意:</strong> 正确使用异步处理,可以参考:
	 * {@link org.springframework.web.servlet.AsyncHandlerInterceptor}.
	 *
	 * @param request 当前 HTTP request
	 * @param response 当前 HTTP response
	 * @param handler 目标handler,可以判断类型和实例
	 * @return {@code true} 继续执行后续的拦截器或handler.否则,DispatcherServlet认为本拦截器已经处理完response。
	 * @throws Exception in case of errors
	 */
	boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	    throws Exception;

	/**
	 * 拦截handler.
	 * 触发时机:HandlerAdapter调用完handler之后,DispatcherServlet渲染视图之前。
     *
	 * 可以操作ModeleAndView内的model对象。
	 *
	 * <p>DispatcherServlet在执行链中处理handler,执行链由任意数量的拦截器和最终的handler组成。
     *  本方法做后处理,按执行链的逆序执行。
	 *
	 * <p><strong>注意:</strong> 正确使用异步处理,可以参考:
	 * {@link org.springframework.web.servlet.AsyncHandlerInterceptor}.
	 *
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler handler (or {@link HandlerMethod}) that started async
	 * execution, for type and/or instance examination
	 * @param modelAndView the {@code ModelAndView} that the handler returned
	 * (can also be {@code null})
	 * @throws Exception in case of errors
	 */
	void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception;

	/**
     * 请求处理完毕回调本方法。
	 * 调用时机:渲染完view。用于清理资源。
	 *
	 * <p>注意: 仅仅当调用了拦截器的{@code preHandle},并且返回true时,才会回调本方法。
     * 调用顺序也是执行链的逆序,也就是第一个拦截器最后调用。
	 *
	 * <p><strong>注意:</strong> 正确使用异步处理,可以参考:
	 * {@link org.springframework.web.servlet.AsyncHandlerInterceptor}.
	 *
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler handler (or {@link HandlerMethod}) that started async
	 * execution, for type and/or instance examination
	 * @param ex exception thrown on handler execution, if any
	 * @throws Exception in case of errors
	 */
	void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception;

}
