package DTO;

import java.util.concurrent.atomic.AtomicInteger;

public class Pet {
	private int id;
	String name;
	private int age;
	private Customer owner;
	private int idType;

	public Pet(int id,String name, int age, Customer owner, int idType) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.owner = owner;
		this.idType = idType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}

//	public static void main(String[] args) {
//		AtomicInteger count = new AtomicInteger();
//		Object s = new Object();
//		while (true) {
//			new Thread(() -> {
//				synchronized(s){
//					count.getAndIncrement();
//					System.err.println("New thread #" + count);
//				}
//				for(;;){
//					try {
//						Thread.sleep(1000);
//					} catch (Exception e){
//						System.err.println(e);
//					}
//				}
//			}).start();
//		}
//	}
}
