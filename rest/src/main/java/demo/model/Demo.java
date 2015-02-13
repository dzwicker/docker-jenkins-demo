package demo.model;

import com.google.common.base.Objects;

public class Demo {

	private int counter;

	public Demo(final int counter) {
		this.counter = counter;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(final int counter) {
		this.counter = counter;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Demo)) {
			return false;
		}

		final Demo restDemo = (Demo) o;

		if (counter != restDemo.counter) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return counter;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("counter", counter)
				.toString();
	}
}
