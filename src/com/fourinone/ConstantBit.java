package com.fourinone;

import java.util.Date;
import java.io.Serializable;
import com.fourinone.FileAdapter.ByteReadParser;
import com.fourinone.FileAdapter.ByteWriteParser;

interface ConstantBit<T> extends CoolHashBase{
	byte[] getBytes(T value);
	T getObject(byte[] bts);
	byte[] getBytes(T value, ByteWriteParser bwp);
	T getObject(byte[] bts, ByteReadParser brp);
	Target tg = Target.POINT;
	enum Target implements ConstantBit,Filter.Condition{
		STRING(DumpAdapter.ConstBit[0],DumpAdapter.ConstBit[0]){
			public boolean matchAction(Object key, Object value){
				Filter<String,Object> filter = (Filter)key;
				Filter.Condition cd = Filter.Condition.Action.valueOf(filter.getFilterKey());
				return cd.matchAction(getObject((byte[])value),filter.getFilterValue());
			}
			
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeChars((String)value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public String getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return (String)getObject(bts);
				brp.reset(bts);
				return brp.readChars(bts.length-1);
			}
		},
		SHORT(DumpAdapter.ConstBit[1],DumpAdapter.ConstBit[1]){
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeShort((short)value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public Short getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return (Short)getObject(bts);
				brp.reset(bts);
				return brp.readShort();
			}
		},
		INTEGER(DumpAdapter.ConstBit[2],DumpAdapter.ConstBit[2]){
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeInt((int)value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public Integer getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return (Integer)getObject(bts);
				brp.reset(bts);
				return brp.readInt();
			}
		},
		FLOAT(DumpAdapter.ConstBit[4],DumpAdapter.ConstBit[2]){
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeFloat((float)value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public Float getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return (Float)getObject(bts);
				brp.reset(bts);
				return brp.readFloat();
			}
		},
		LONG(DumpAdapter.ConstBit[3],DumpAdapter.ConstBit[3]){
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeLong((long)value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public Long getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return (Long)getObject(bts);
				brp.reset(bts);
				return brp.readLong();
			}
		},
		DOUBLE(DumpAdapter.ConstBit[5],DumpAdapter.ConstBit[3]){
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeDouble((double)value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public Double getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return (Double)getObject(bts);
				brp.reset(bts);
				return brp.readDouble();
			}
		},
		DATE(DumpAdapter.ConstBit[6],DumpAdapter.ConstBit[3]){
			public boolean matchAction(Object key, Object value){
				return LONG.matchAction(key,value);
			}
			
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeDate((Date)value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public Date getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return (Date)getObject(bts);
				brp.reset(bts);
				return brp.readDate();
			}
		},
		STATGOBJECT(DumpAdapter.ConstBit[7],DumpAdapter.ConstBit[0]){
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				bwp.reset();
				bwp.writeObject(value);
				bwp.writeBytes(new byte[]{(byte)codeKey});
				return bwp.getBytes();
			}
			
			public Object getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return getObject(bts);
				brp.reset(bts);
				return brp.readObject(bts.length-1);
			}
		},
		POINT(DumpAdapter.ConstBit[8],DumpAdapter.ConstBit[0]){
			public boolean matchAction(Object key, Object value){
				return STRING.matchAction(key,value);
			}
			
			public byte[] getBytes(Object value, ByteWriteParser bwp){
				if(bwp==null)
					return getBytes(value);
				byte[] bts = STRING.getBytes(value, bwp);
				bts[bts.length-1]=(byte)codeKey;
				return bts;
			}
			
			public Object getObject(byte[] bts, ByteReadParser brp){
				if(brp==null)
					return getObject(bts);
				return STRING.getObject(bts, brp);
			}
		};
		
		public byte[] getBytes(Object value){
			return getBytes(value, bwp);
		}
		
		public Object getObject(byte[] bts){
			return getObject(bts, brp);
		}		
		
		static Target getTarget(byte codeKey){
			for(Target tg:Target.values())
				if(tg.codeKey==codeKey)
					return tg;
			return null;
		}
		
		private static <T> Target getTarget(Class<T> value, boolean match){
			try{
				return Target.valueOf(value.getSimpleName().toUpperCase());
			}catch(Exception e){
				if(!match&&Serializable.class.isAssignableFrom(value))
					return STATGOBJECT;
				else
					LogUtil.info("[CoolHashException]","[IllegalArgumentException]",chex.checkTargetLog(value));
			}
			return null;
		}
		
		static <T> Target getTarget(Class<T> value){
			return getTarget(value, false);
		}
		
		static <T> Target getTarget(T value){
			Target tg=getTarget(value.getClass());
			return tg;
		}
		
		static <T> Target getTargetMatch(Class<T> value){
			return getTarget(value, true);
		}
		
		static <T> byte[] getTargetBytes(T value){
			/*ConstantBit<T> cb = ConstantBit.Target.getTarget(value);
			byte[] bts = cb.getBytes(value);
			return bts;*/
			return getTargetBytes(value, bwp);
		}
	
		static <T> byte[] getTargetBytes(T value, ByteWriteParser bwp){
			ConstantBit<T> cb = ConstantBit.Target.getTarget(value);
			byte[] bts = bwp==null?cb.getBytes(value):cb.getBytes(value, bwp);
			return bts;
		}
		
		static <T> T getTargetObject(byte[] bts){
			return getTargetObject(bts,null,brp);
		}
		
		static <T> T getTargetObject(byte[] bts, Class<T> value){
			/*if(bts==null||bts.length==0){
				return null;
			}else{
				ConstantBit<T> cb=value!=null?ConstantBit.Target.getTarget(value):ConstantBit.Target.getTarget(bts[bts.length-1]);
				return cb.getObject(bts);
			}*/
			return getTargetObject(bts, value, brp);
		}

		static <T> T getTargetObject(byte[] bts, ByteReadParser brp){
			return getTargetObject(bts, null, brp);
		}
			
		static <T> T getTargetObject(byte[] bts, Class<T> value, ByteReadParser brp){
			if(bts==null||bts.length==0){
				return null;
			}else{
				ConstantBit<T> cb=value!=null?ConstantBit.Target.getTarget(value):ConstantBit.Target.getTarget(bts[bts.length-1]);
				return brp==null?cb.getObject(bts):cb.getObject(bts, brp);
			}
		}
		
		public boolean matchAction(Object key, Object value){
			Filter<String,Object> filter = (Filter)key;
			byte[] vb = (byte[])value;
			if(vb.length-1==codeValue&&(vb[vb.length-1]==codeKey||(vb[vb.length-1]==DumpAdapter.ConstBit[6]&&codeKey==DumpAdapter.ConstBit[3]))){//+1? type==type
				Filter.Condition cd = Filter.Condition.Action.valueOf(filter.getFilterKey());
				return cd.matchAction(getObject(vb),filter.getFilterValue());
			}
			return false;
		}
		
		int codeKey;
		int codeValue;
		Target(int codeKey, int codeValue){
			this.codeKey = codeKey;
			this.codeValue = codeValue;
		}
	}
}