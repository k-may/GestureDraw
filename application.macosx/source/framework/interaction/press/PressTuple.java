package framework.interaction.press;

public class PressTuple<H, T> {
	private final H left;
	private final T right;

	public PressTuple(H left, T right) {
		this.left = left;
		this.right = right;
	}

	public H getHandId() {
		return left;
	}

	public T getTargetId() {
		return right;
	}

	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof PressTuple))
			return false;
		PressTuple pairo = (PressTuple) o;
		return this.left.equals(pairo.getHandId())
				&& this.right.equals(pairo.getTargetId());
	}
}
