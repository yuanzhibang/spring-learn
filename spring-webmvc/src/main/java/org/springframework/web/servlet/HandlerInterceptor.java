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
 * Workflow�ӿ�������handlerִ����,
 * �������ע��һЩ�Դ������������Ƶ�������,��������������һ��handler,
 * �����ڲ��޸���һ��handlerʵ�ֵ�ǰ�������һЩͨ�õ���Ϊ��
 *
 * <p>�ڶ�Ӧ��HandlerAdapter����handler֮ǰ,HandlerInterceptor�ᱻ���á�
 * ���ֻ���Ӧ�õ��ܶ�Ԥ����ĳ���,����:Ȩ��У��,���ʻ�,�����л���
 * ����Ҫ��Ŀ���Ƿֽ��handler���ظ����롣
 *
 * <p>��һ���첽��������, �����߳�û����Ⱦmodel����ִ��{@code postHandle}��{@code afterCompletion}�ص�ʱ,
 * handler���ܻ�����һ�������߳���ִ�С���handler����ִ�����,�����ת������ִ����Ⱦmodel�ķ�����
 * ����: {@code org.springframework.web.servlet.AsyncHandlerInterceptor}
 *
 * <p>һ�������,һ��HandlerMapping bean��Ӧһ������������
 * ����������һ��bean,HandlerMapping beanͨ��interceptors��������������bean��
 * XML������,interceptors������һ��list,Ҳ������һ��ref��
 *
 * <p>HandlerInterceptor������Servlet������,
 * Servlet����������ǿ��,�������ͨ������request��response�ڵĶ���,��ǰ������������
 * ��ȹ�Servlcet����,HandlerInterceptor��������Ԥ����ͺ���,ͨ��ѡ���ԵĽ���handlerִ�С�
 * ע��:Servlet������������web.xml��,HandlerInterceptor��Application Context�
 *
 * <p> ������ָ��˼��:�����ϸ���ȵ�,����handler�Ĵ�������,HanlderInterceptorʵ�ֶ���Щ������д���,
 * �ر���Ҫ��ȡ��ͨ�õ�handler�����Ȩ��У�顣
 * ����,Servlet���������ʺϴ���request���ݺ���ͼ����,  ����,���ϱ���GZIPѹ����
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
	 * ����handlerִ�С�
	 * ������ִ��ʱ��:HandlerMapping�Ѿ�ȷ������Ӧ��handler����֮��,HandleAdapter����Handler֮ǰ��
	 *
	 * <p>DispatcherServlet��ִ�����д���handler,ִ���������������������������յ�handler��ɡ�
     * ���������Դ��ִ����,���緢��һ��HTTP�������ֱ����responseд�����ݡ�
	 *
	 * <p><strong>ע��:</strong> ��ȷʹ���첽����,���Բο�:
	 * {@link org.springframework.web.servlet.AsyncHandlerInterceptor}.
	 *
	 * @param request ��ǰ HTTP request
	 * @param response ��ǰ HTTP response
	 * @param handler Ŀ��handler,�����ж����ͺ�ʵ��
	 * @return {@code true} ����ִ�к�������������handler.����,DispatcherServlet��Ϊ���������Ѿ�������response��
	 * @throws Exception in case of errors
	 */
	boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	    throws Exception;

	/**
	 * ����handler.
	 * ����ʱ��:HandlerAdapter������handler֮��,DispatcherServlet��Ⱦ��ͼ֮ǰ��
     *
	 * ���Բ���ModeleAndView�ڵ�model����
	 *
	 * <p>DispatcherServlet��ִ�����д���handler,ִ���������������������������յ�handler��ɡ�
     *  ������������,��ִ����������ִ�С�
	 *
	 * <p><strong>ע��:</strong> ��ȷʹ���첽����,���Բο�:
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
     * ��������ϻص���������
	 * ����ʱ��:��Ⱦ��view������������Դ��
	 *
	 * <p>ע��: ��������������������{@code preHandle},���ҷ���trueʱ,�Ż�ص���������
     * ����˳��Ҳ��ִ����������,Ҳ���ǵ�һ�������������á�
	 *
	 * <p><strong>ע��:</strong> ��ȷʹ���첽����,���Բο�:
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
