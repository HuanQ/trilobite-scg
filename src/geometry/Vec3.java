/*
 * $RCSfile$
 *
 * Copyright 1997-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 *
 * $Revision$
 * $Date$
 * $State$
 */

package geometry;

import java.lang.Math;

/**
 * A generic 3-element tuple that is represented by single-precision  
 * floating point x,y,z coordinates.
 *
 */
public class Vec3 implements java.io.Serializable, Cloneable {
	public static final Vec3						white = new Vec3(1, 1, 1);
	public static final Vec3						lightgray = new Vec3(0.75f, 0.75f, 0.75f);
	public static final Vec3						gray = new Vec3(0.5f, 0.5f, 0.5f);
	public static final Vec3						darkgray = new Vec3(0.25f, 0.25f, 0.25f);	
	public static final Vec3						black = new Vec3(0, 0, 0);
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8599531954450231729L;


	/**
     * The x coordinate.
     */
    public	float	x;


    /**
     * The y coordinate.
     */
    public	float	y;


    /**
     * The z coordinate.
     */
    public	float	z;


    /**
     * Constructs and initializes a Vec2 from the specified xy coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vec3(float x, float y, float z)
    {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }


    /**
     * Constructs and initializes a Tuple2f from the specified Vec3.
     * @param t1 the Vec3 containing the initialization x y z data
     */
    public Vec3(Vec3 t1)
    {
    	this.x = t1.x;
    	this.y = t1.y;
    	this.z = t1.z;
    }


    /**
     * Constructs and initializes a Vec3 to (0,0,0).
     */
    public Vec3()
    {
    	this.x = 0.0f;
    	this.y = 0.0f;
    	this.z = 0.0f;
    }


    /**
     * Sets the value of this tuple to the specified xy coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void set(float x, float y, float z)
    {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }


    /**
     * Sets the value of this tuple to the value of the Vec3 argument.
     * @param t1 the tuple to be copied
     */
    public void set(Vec3 t1)
    {
    	this.x = t1.x;
    	this.y = t1.y;
    	this.z = t1.z;
    }


    /**
     * Checks if it is all ones
     * @returns true if it is ones
     */
    public boolean isOnes()
    {
    	return x == 1 && y == 1 && z == 1;
    }


    /**
     * Checks if it is zero
     * @returns true if it is zero
     */
    public boolean isZero()
    {
    	return x == 0 && y == 0 && z == 0;
    }


    /**
     * Sets to zero
     */
    public void zero()
    {
    	x = 0;
    	y = 0;
    	z = 0;
    }

  
    /**
     * Sets the value of this tuple to the vector sum of tuples t1 and t2.
     * @param t1 the first tuple
     * @param t2 the second tuple
     */
    public void add(Vec3 t1, Vec3 t2)
    {
    	this.x = t1.x + t2.x;
    	this.y = t1.y + t2.y;
    	this.z = t1.z + t2.z;
    }


    /**
     * Sets the value of this tuple to the vector sum of itself and tuple t1.
     * @param t1 the other tuple
     */  
    public void add(Vec3 t1)
    {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }


    /**
     * Sets the value of this tuple to the vector multiplication of itself and tuple t1.
     * @param t1 the other tuple
     */  
    public void mult(Vec3 t1)
    {
        this.x *= t1.x;
        this.y *= t1.y;
        this.z *= t1.z;
    }

    
    /**
     * Sets the value of this tuple to the vector multiplication of tuples t1 and t2.
     * @param t1 the first tuple
     * @param t2 the second tuple
     */
    public void mult(Vec3 t1, Vec3 t2)
    {
    	this.x = t1.x * t2.x;
    	this.y = t1.y * t2.y;
    	this.z = t1.z * t2.z;
    }


    /**
     * Sets the value of this tuple to the vector difference of 
     * tuple t1 and t2 (this = t1 - t2).    
     * @param t1 the first tuple
     * @param t2 the second tuple
     */  
    public void sub(Vec3 t1, Vec3 t2)
    {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
    }  


    /**
     * Sets the value of this tuple to the vector difference of
     * itself and tuple t1 (this = this - t1).
     * @param t1 the other tuple
     */  
    public void sub(Vec3 t1)
    {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
    }


    /**
     * Sets the value of this tuple to the negation of tuple t1.
     * @param t1 the source tuple
     */
    public void negate(Vec3 t1)
    {
    	this.x = -t1.x;
    	this.y = -t1.y;
    	this.z = -t1.z;
    }


    /**
     * Negates the value of this vector in place.
     */
    public void negate()
    {
    	this.x = -this.x;
    	this.y = -this.y;
    	this.z = -this.z;
    }


    /**
     * Sets the value of this tuple to the scalar multiplication
     * of tuple t1.
     * @param s the scalar value
     * @param t1 the source tuple
     */
    public final void scale(float s, Vec3 t1)
    {
    	this.x = s*t1.x;
    	this.y = s*t1.y;
    	this.z = s*t1.z;
    }


    /**
     * Sets the value of this tuple to the scalar multiplication
     * of itself.
     * @param s the scalar value
     */
    public final void scale(float s)
    {
    	this.x *= s;
    	this.y *= s;
    	this.z *= s;
    }


    /**
     * Returns a tuple to the scalar multiplication of this vector.
     * @param s the scalar value
     * @return the result
     */
    public final Vec3 getScaled(float s)
    {
    	return new Vec3(x*s, y*s, z*s);
    }


    /**
     * Sets the value of this tuple to the scalar multiplication
     * of tuple t1 and then adds tuple t2 (this = s*t1 + t2).
     * @param s the scalar value
     * @param t1 the tuple to be multipled
     * @param t2 the tuple to be added
     */  
    public final void scaleAdd(float s, Vec3 t1, Vec3 t2)
    {
        this.x = s*t1.x + t2.x; 
        this.y = s*t1.y + t2.y;
        this.z = s*t1.z + t2.z;
    } 
 

    /**
     * Sets the value of this tuple to the scalar multiplication
     * of itself and then adds tuple t1 (this = s*this + t1).
     * @param s the scalar value
     * @param t1 the tuple to be added
     */
    public final void scaleAdd(float s, Vec3 t1)
    {
        this.x = s*this.x + t1.x;
        this.y = s*this.y + t1.y;
        this.z = s*this.z + t1.z;
    }


    /**
     * Returns a hash code value based on the data values in this
     * object.  Two different Tuple2f objects with identical data values
     * (i.e., Tuple2f.equals returns true) will return the same hash
     * code value.  Two objects with different data members may return the
     * same hash value, although this is not likely.
     * @return the integer hash code value
     */  
    public int hashCode() {
    	long bits = 1L;
    	bits = 31L * bits + (long)floatToIntBits(x);
    	bits = 31L * bits + (long)floatToIntBits(y);
    	bits = 31L * bits + (long)floatToIntBits(z);
    	return (int) (bits ^ (bits >> 32));
    }


    /**   
     * Returns true if all of the data members of Tuple2f t1 are
     * equal to the corresponding data members in this Tuple2f.
     * @param t1  the vector with which the comparison is made
     * @return  true or false
     */  
    public boolean equals(Vec3 t1)
    {
        try {
           return(this.x == t1.x && this.y == t1.y && this.z == t1.z);
        }
        catch (NullPointerException e2) {return false;}

    }

    /**   
     * Returns true if the Object t1 is of type Vec2 and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Vec2.
     * @param t1  the object with which the comparison is made
     * @return  true or false
     */  
    public boolean equals(Object t1)
    {
        try {
           Vec3 t2 = (Vec3) t1;
           return(this.x == t2.x && this.y == t2.y && this.z == t2.z);
        }
        catch (NullPointerException e2) {return false;}
        catch (ClassCastException   e1) {return false;}

    }

	/**
     * Returns true if the L-infinite distance between this tuple
     * and tuple t1 is less than or equal to the epsilon parameter, 
     * otherwise returns false.  The L-infinite
     * distance is equal to MAX[abs(x1-x2), abs(y1-y2)]. 
     * @param t1  the tuple to be compared to this tuple
     * @param epsilon  the threshold value  
     * @return  true or false
     */
    public boolean epsilonEquals(Vec3 t1, float epsilon)
    {
       float diff;

       diff = x - t1.x;
       if(Float.isNaN(diff)) return false;
       if((diff<0?-diff:diff) > epsilon) return false;

       diff = y - t1.y;
       if(Float.isNaN(diff)) return false;
       if((diff<0?-diff:diff) > epsilon) return false;
       
       diff = z - t1.z;
       if(Float.isNaN(diff)) return false;
       if((diff<0?-diff:diff) > epsilon) return false;

       return true;
    }

	/**
	 * Returns a string that contains the values of this Tuple2f.
	 * The form is (x,y).
	 * @return the String representation
	 */  
	public String toString()
	{
        return("(" + this.x + ", " + this.y + ", " + this.z + ")");
	}


	/**
	 *  Clamps the tuple parameter to the range [low, high] and 
	 *  places the values into this tuple.  
	 *  @param min  the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping 
	 *  @param t   the source tuple, which will not be modified
	 */
	public final void clamp(Vec3 min, Vec3 max)
	{
		x = x > max.x ? max.x : x < min.x ? min.x : x;
        y = y > max.y ? max.y : y < min.y ? min.y : y;
        z = z > max.z ? max.z : z < min.z ? min.z : z;
	}


	/**
	 *  Clamps the tuple parameter to the range [low, high] and 
	 *  places the values into this tuple.  
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping 
	 *  @param t   the source tuple, which will not be modified
	 */
	public final void clamp(float min, float max, Vec3 t)
	{
		x = t.x > max ? max : t.x < min ? min : t.x;
		y = t.y > max ? max : t.y < min ? min : t.y;
		z = t.z > max ? max : t.z < min ? min : t.z;
	}


	/** 
	 *  Clamps the minimum value of the tuple parameter to the min 
	 *  parameter and places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping 
	 *  @param t   the source tuple, which will not be modified
	 */   
	public final void clampMin(float min, Vec3 t) 
	{ 
		x = t.x < min ? min : t.x;
		y = t.y < min ? min : t.y;
		z = t.z < min ? min : t.z;
	} 


	/**  
	 *  Clamps the maximum value of the tuple parameter to the max 
	 *  parameter and places the values into this tuple.
	 *  @param max   the highest value in the tuple after clamping  
	 *  @param t   the source tuple, which will not be modified
	 */    
	public final void clampMax(float max, Vec3 t)  
	{  
		x = t.x > max ? max : t.x;
		y = t.y > max ? max : t.y;
		z = t.z > max ? max : t.z;
   } 


	/**  
	 *  Sets each component of the tuple parameter to its absolute 
	 *  value and places the modified values into this tuple.
	 *  @param t   the source tuple, which will not be modified
	 */    
	public final void absolute(Vec3 t)
	{
		x = Math.abs(t.x);
		y = Math.abs(t.y);
		z = Math.abs(t.z);
	} 


	/**
	 *  Clamps this tuple to the range [low, high].
	 *  @param min  the lowest value in this tuple after clamping
	 *  @param max  the highest value in this tuple after clamping
	 */
	public final void clamp(float min, float max)
	{
        if( x > max ) {
          x = max;
        } else if( x < min ){
          x = min;
        }
 
        if( y > max ) {
          y = max;
        } else if( y < min ){
          y = min;
        }

        if( z > max ) {
          z = max;
        } else if( z < min ){
          z = min;
        }

	}

 
	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	public final void clampMin(float min)
	{ 
      if( x < min ) x=min;
      if( y < min ) y=min;
      if( z < min ) z=min;
	} 
 
 
	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	public final void clampMax(float max)
	{ 
		if( x > max ) x=max;
		if( y > max ) y=max;
		if( z > max ) z=max;
	}


	/**
	 *  Sets each component of this tuple to its absolute value.
	 */
	public final void absolute()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
	}


	/** 
	 *  Linearly interpolates between tuples t1 and t2 and places the 
	 *  result into this tuple:  this = (1-alpha)*t1 + alpha*t2.
	 *  @param t1  the first tuple
	 *  @param t2  the second tuple
	 *  @param alpha  the alpha interpolation parameter
	 */
	public final void interpolate(Vec3 t1, Vec3 t2, float alpha)
	{
		this.x = (1-alpha)*t1.x + alpha*t2.x;
		this.y = (1-alpha)*t1.y + alpha*t2.y;
		this.z = (1-alpha)*t1.z + alpha*t2.z;
	}


	/**  
	 *  Linearly interpolates between this tuple and tuple t1 and 
	 *  places the result into this tuple:  this = (1-alpha)*this + alpha*t1.
	 *  @param t1  the first tuple
	 *  @param alpha  the alpha interpolation parameter  
	 */   
	public final void interpolate(Vec3 t1, float alpha) 
	{ 
		this.x = (1-alpha)*this.x + alpha*t1.x;
		this.y = (1-alpha)*this.y + alpha*t1.y;
		this.z = (1-alpha)*this.z + alpha*t1.z;
	} 

	/**
     * Creates a new object of the same class as this object.
     *
     * @return a clone of this instance.
     * @exception OutOfMemoryError if there is not enough memory.
     * @see java.lang.Cloneable
     * @since vecmath 1.3
     */
	public Vec3 clone() {
    	// Since there are no arrays we can just use Object.clone()
    	try {
    		return (Vec3) super.clone();
    	} catch (CloneNotSupportedException e) {
    		// this shouldn't happen, since we are Cloneable
    		throw new InternalError();
    	}
    }


	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param v1 the other vector
	 */
	public final float dot(Vec3 v1)
	{
		return (this.x*v1.x + this.y*v1.y + this.z*v1.z);
	}


	/**  
	 * Returns the distance from one vector to another.
	 * @return the distance
	 */  
	public final float distance(Vec3 v1)
	{
		float difX = v1.x-this.x;
		float difY = v1.y-this.y;
		float difZ = v1.z-this.z;
		return (float) Math.sqrt( difX*difX + difY*difY + difZ*difZ );
	}


	/**  
	 * Returns the squared distance from one vector to another.
	 * @return the squared distance
	 */  
	public final float distanceSquared(Vec3 v1)
	{
		float difX = v1.x-this.x;
		float difY = v1.y-this.y;
		float difZ = v1.z-this.z;
		return difX*difX + difY*difY + difZ*difZ;
	}


	/**  
	 * Returns the length of this vector.
	 * @return the length of this vector
	 */  
	public final float length()
	{
		return (float) Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}


	/**  
	 * Returns the squared length of this vector.
	 * @return the squared length of this vector
	 */  
	public final float lengthSquared()
	{
		return (this.x*this.x + this.y*this.y + this.z*this.z);
	}

	/**  
	 * Returns the XY length of this vector.
	 * @return the XT length of this vector
	 */  
	public final float XYlength()
	{
		return (float) Math.sqrt(this.x*this.x + this.y*this.y);
	}


	/**  
	 * Returns the squared XY length of this vector.
	 * @return the squared XY length of this vector
	 */  
	public final float XYlengthSquared()
	{
		return (this.x*this.x + this.y*this.y);
	}

	/**
	 * Sets the value of this vector to the normalization of vector v1.
	 * @param v1 the un-normalized vector
	 */  
	public final void normalize(Vec3 v1)
	{
		float norm;

		norm = (float) (1.0/Math.sqrt(v1.x*v1.x + v1.y*v1.y + v1.z*v1.z));
		this.x = v1.x*norm;
		this.y = v1.y*norm;
		this.z = v1.z*norm;
	}


	/**
	 * Normalizes this vector in place.
	 */  
	public final void normalize()
	{
		float norm;

		norm = (float) (1.0/Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z));
		this.x *= norm;
		this.y *= norm;
		this.z *= norm;
	}


	/**
	 *   Returns the angle in radians between this vector and the vector
	 *   parameter; the return value is constrained to the range [0,PI].
	 *   @param v1    the other vector
	 *   @return   the angle in radians in the range [0,PI]
	 */
	public final float angle(Vec3 v1)
	{
		double vDot = this.dot(v1) / ( this.length()*v1.length() );
		if( vDot < -1.0) vDot = -1.0;
		if( vDot >  1.0) vDot =  1.0;
		return((float) (Math.acos( vDot )));
	}


	/**
     * Returns the representation of the specified floating-point
     * value according to the IEEE 754 floating-point "single format"
     * bit layout, after first mapping -0.0 to 0.0. This method is
     * identical to Float.floatToIntBits(float) except that an integer
     * value of 0 is returned for a floating-point value of
     * -0.0f. This is done for the purpose of computing a hash code
     * that satisfies the contract of hashCode() and equals(). The
     * equals() method in each vecmath class does a pair-wise "=="
     * test on each floating-point field in the class (e.g., x, y, and
     * z for a Tuple3f). Since 0.0f&nbsp;==&nbsp;-0.0f returns true,
     * we must also return the same hash code for two objects, one of
     * which has a field with a value of -0.0f and the other of which
     * has a cooresponding field with a value of 0.0f.
     *
     * @param f an input floating-point number
     * @return the integer bits representing that floating-point
     * number, after first mapping -0.0f to 0.0f
     */
	private int floatToIntBits(float f) {
		// Check for +0 or -0
		if (f == 0.0f) {
		    return 0;
		}
		else {
		    return Float.floatToIntBits(f);
		}
		
    }
}
