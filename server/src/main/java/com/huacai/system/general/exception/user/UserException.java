package com.huacai.system.general.exception.user;

import com.huacai.system.general.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author huacai
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
