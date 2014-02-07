package common.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class can be used for reflection purpose. Both class should match the getter and setter.
 * @author Vijay Shegokar
 * @version 1.0
 */
public class ReflectionUtil {
	
	/**
	 * This method is used to copy one bean values to another bean.
	 * @param toClazz Class object of Destination bean, which will be used to create class instance to copy values from passed object
	 * @param from Origin bean from where to copy values
	 * @param strictCopy boolean value denoting that copy should be in strict manner or not. If this is true then null values will also copied to destination bean from origin bean
	 * @throws IllegalArgumentException If passed bean is null.
	 * @throws InvocationTargetException If method access is denied
	 * @throws InstantiationException If not able to create an instance of an passed toClass
	 * @throws IllegalAccessException If access denied to access given class
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copy(Class<T> toClazz, Object from, boolean strictCopy) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (toClazz == null)
			throw new IllegalArgumentException("No destination bean specified");
		Object to = toClazz.newInstance();
		copy(to, from, strictCopy);
		return (T)to;
	}
	
	/**
	 * This method is used to copy one bean values to another bean.
	 * @param toClazz Class object of Destination bean, which will be used to create class instance to copy values from passed object
	 * @param from Origin bean from where to copy values
	 * @throws IllegalArgumentException If passed bean is null.
	 * @throws InvocationTargetException If method access is denied
	 * @throws InstantiationException If not able to create an instance of an passed toClass
	 * @throws IllegalAccessException If access denied to access given class
	 */
	public static <T> T copy(Class<T> toClazz, Object from) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return copy(toClazz, from, false);
	}

	/**
	 * This method is used to copy one bean values to another bean.
	 * @param to : Destination bean where values need to be copy from origin
	 * @param from : Origin bean from where to copy values
	 * @throws IllegalArgumentException If passed bean is null.
	 * @throws InvocationTargetException If method access is denied
	 */
	public static void copy(final Object to, final Object from) throws IllegalArgumentException, InvocationTargetException {
		copy(to, from, false);
	}
	
	/**
	 * This method is used to copy one bean values to another bean. 
	 * @param to : Origin bean from where to copy values
	 * @param from : Destination bean where values need to be copy from origin
	 * @param strictCopy boolean value denoting that copy should be in strict manner or not. If this is true then null values will also copied to destination bean from origin bean
	 * @throws IllegalArgumentException If passed bean is null.
	 * @throws InvocationTargetException If method access is denied
	 */
	synchronized public static void copy(final Object to, final Object from, boolean strictCopy) throws IllegalArgumentException, InvocationTargetException{
		// long t1 = System.currentTimeMillis();
		// System.out.println("Time Sarted : " + t1);
		// Validate existence of the specified beans
		if (to == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (from == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}
		try {
			Class<? extends Object> toClass = to.getClass();
			Method[] toMethods = toClass.getMethods();
			Set<Method> toMethodsList = new LinkedHashSet<Method>(Arrays.asList(toMethods));

			Class<? extends Object> fromClass = from.getClass();
			Method[] fromMethods = fromClass.getMethods();
			Set<Method> fromMethodsList = new LinkedHashSet<Method>(Arrays.asList(fromMethods));
			
			Iterator<Method> fromMethodsIterator = fromMethodsList.iterator();
			while(fromMethodsIterator.hasNext()) {
				Method fromMethod = fromMethodsIterator.next();
				boolean isBoolean = false;
				Class<?> returnType = fromMethod.getReturnType();
				if(fromMethod.getName().startsWith("is") && boolean.class.equals(returnType)) {
					isBoolean = true;
				} 
				if (isGetter(fromMethod, isBoolean)) {
					Object value = null;
					try {
						value = fromMethod.invoke(from);
					} catch (Exception e) {
						throw new InvocationTargetException(e, "Getter method cannot have any parameter. Error on method : " + fromMethod.getName());
					}
					if(strictCopy){
						setValues(returnType, value, fromMethod, to, toMethodsList, isBoolean);
					} else {
						if(value != null) {
							setValues(returnType, value, fromMethod, to, toMethodsList, isBoolean);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		// long t2 = System.currentTimeMillis();
		// System.out.println("Time completed : " + t2);
		// System.out.println("Total Time completed : " + (t2 - t1));
	}

	/**
	 * This method is used to check method is getter or not.
	 * @param method Method which needs to be check
	 * @param isBoolean If the given method is primitive boolean type because getter for it starts with "is".
	 * @return true is method is getter
	 */
	public static boolean isGetter(Method method, boolean isBoolean) {
		if(isBoolean && !method.getName().startsWith("is"))
			return false;
		if (!isBoolean && !method.getName().startsWith("get"))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	/**
	 * This method is used to check method is setter or not.
	 * @param method Method which needs to be check
	 * @return true is method is setter
	 */
	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set"))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		return true;
	}

	/**
	 * This method set the values to destination bean
	 * @param returnType Return type of from class method
	 * @param value value of from class getter method to be copied to destination
	 * @param fromMethod method of from class
	 * @param to destination class object
	 * @param toMethodsList set of to class all methods
	 * @param isBoolean method return type is boolean or not
	 * @throws IllegalArgumentException If passed bean is null.
	 * @throws InvocationTargetException If method access is denied
	 * @throws IllegalAccessException If access denied to access given class
	 */
	public static void setValues(Class<?> returnType, Object value, final Method fromMethod, final Object to, Set<Method> toMethodsList, boolean isBoolean) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!returnType.isPrimitive()) {
			fromMethod.getReturnType().cast(value);
		} 
		Iterator<Method> toMethodsIterator = toMethodsList.iterator();
		while (toMethodsIterator.hasNext()) {
			Method toMethod = (Method) toMethodsIterator.next();
			if (isSetter(toMethod)) {
				int fromSubstringIndex = 3; 
				if(isBoolean)
					fromSubstringIndex = 2;
				if(toMethod.getName().substring(3).equals(fromMethod.getName().substring(fromSubstringIndex))) {
					try {
						toMethod.invoke(to, value);
					} catch (Exception e) {
						throw new InvocationTargetException(e, "Data type mismatched or access specifier is wrong. Error on method: TO method = " + toMethod.getName() + " FROM method = " + fromMethod.getName());
					}
					toMethodsList.remove(toMethod);
					break;
				}
			}
		}
	}
	
}
