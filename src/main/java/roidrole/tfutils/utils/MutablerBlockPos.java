package roidrole.tfutils.utils;

import net.minecraft.util.math.BlockPos;

public class MutablerBlockPos extends BlockPos.MutableBlockPos {
	public MutablerBlockPos() {
		super();
	}

	public void addX(int amount){
		this.x += amount;
	}

	public void addY(int amount){
		this.y += amount;
	}

	public void addZ(int amount){
		this.z += amount;
	}

	public void addX(){
		this.x++;
	}

	public void addY(){
		this.y++;
	}

	public void addZ(){
		this.z++;
	}

	public void setX(int x){
		this.x = x;
	}

	//setY is already in BlockPos and modifies the pos

	public void setZ(int z){
		this.z = z;
	}

	public void translate(int x, int y, int z){
		this.x += x;
		this.y += y;
		this.z += z;
	}
}
