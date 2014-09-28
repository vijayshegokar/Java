package common.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class can be used for reflection purpose. Both class should match the getter and setter.
 * @author Vijay Shegokar
 * @version 1.1
 * @date 28-September-2014
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
	public static <T> T copy(Class<T> toClazz, Object from, boolean strictCopy) throws Exception {
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
	public static <T> T copy(Class<T> toClazz, Object from) throws Exception {
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
							System.out.println(fromMethod.getName());
							setValues(returnType, value, fromMethod, to, toMethodsList, isBoolean);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
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
						System.out.println(toMethod.getName());
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
	
	/**
	 * This method put the given field value into given object
	 * @param obj - object where value has to be put
	 * @param clazz - class of passed object
	 * @param fieldName - {@link String} field(variable) name
	 * @param value - value which has to be set
	 */
	public static <T> void findPut(Object obj, Class<T> clazz, String fieldName, Object value){
		if(clazz.isInstance(obj)) {
			clazz.cast(obj);
		}
		try {
			Field field = clazz.getDeclaredField(fieldName);
			String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method setter = clazz.getMethod(methodName, field.getType());
			setter.invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * This method put the given map string key field key with their respective value into given object
	 * @param obj - object where value has to be put
	 * @param map - {@link Map} String key and Object value map, where key will be field name and object will be value for that field.
	 */
	public static void findNPut(Object obj, Map<String, Object> map){
		if(obj == null || map == null || map.size() == 0) {
			System.err.println("Invalid Input.");
			return;
		}
		Class<?> clazz = obj.getClass();
		if(clazz.isInstance(obj)) {
			try {
				clazz.cast(obj);
				Set<String> fields = map.keySet();
				for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
					String variable = (String) iterator.next();
					Field field = null;
					try {
						field = clazz.getDeclaredField(variable);
					} catch (Exception e) {
						System.err.println(variable + " : field not found in this class hence finding in its super class.");
						try {
							field = clazz.getSuperclass().getDeclaredField(variable);
						} catch (Exception e1) {
							System.err.println(variable + " : field also not found in super class, hence skipping it.");
							continue;
						}
					}
					if(field != null) {
						Method setter = null;
						String methodName = "set" + variable.substring(0, 1).toUpperCase() + variable.substring(1);
						try {
							setter = clazz.getDeclaredMethod(methodName, field.getType());
						} catch (Exception e) {
							System.err.println(methodName + " : method not found in this class hence finding in its super class.");
							try {
								setter = clazz.getSuperclass().getDeclaredMethod(methodName, field.getType());
							} catch (Exception e1) {
								System.err.println(methodName + " : method also not found in super class, hence skipping it.");
								continue;
							} 
						}
						if(setter != null) {
							try {
								setter.invoke(obj, map.get(variable));
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Cannot cast passed object " + obj + " to given " + clazz + " Class.");
			}
		} else {
			System.err.println("Sent Object " + obj + " is not an instance of passed " + clazz + " Class.");
		}
	}
	
}
