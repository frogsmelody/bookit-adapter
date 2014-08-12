package com.derbysoft.bookit.push.commons.loginterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import org.junit.Ignore;

/**
 * Author: Jason Wu
 * Date  : 2014-04-22
 */
@Ignore
public class ProxyMethodInvocationImpl implements ProceedingJoinPoint {
    private Object[] arguments;
    private Object response;
    private Exception exception;

    public ProxyMethodInvocationImpl(Object[] arguments, Object response) {
        this.arguments = arguments;
        this.response = response;
    }

    public ProxyMethodInvocationImpl(Object[] arguments, Exception exception) {
        this.arguments = arguments;
        this.exception = exception;
    }

    @Override
    public void set$AroundClosure(AroundClosure arc) {

    }

    @Override
    public Object proceed() throws Throwable {
        if (exception != null) {
            throw exception;
        }
        return response;
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        return null;
    }

    @Override
    public String toShortString() {
        return null;
    }

    @Override
    public String toLongString() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Object[] getArgs() {
        return arguments;
    }

    @Override
    public Signature getSignature() {
        return null;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return null;
    }

    @Override
    public String getKind() {
        return null;
    }

    @Override
    public StaticPart getStaticPart() {
        return null;
    }
}
