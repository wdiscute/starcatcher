package com.wdiscute.starcatcher.io;

import com.mojang.datafixers.util.*;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class ExtraComposites
{
    public static <B, C, T1, T2, T3, T4, T5, T6> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final Function6<T1, T2, T3, T4, T5, T6, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                T11 t11 = codec11.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
                codec11.encode(encode, getter11.apply(apply));
            }
        };
    }


    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12,
            final Function<C, T12> getter12,
            final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                T11 t11 = codec11.decode(decode);
                T12 t12 = codec12.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
                codec11.encode(encode, getter11.apply(apply));
                codec12.encode(encode, getter12.apply(apply));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12,
            final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13,
            final Function<C, T13> getter13,
            final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                T11 t11 = codec11.decode(decode);
                T12 t12 = codec12.decode(decode);
                T13 t13 = codec13.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
                codec11.encode(encode, getter11.apply(apply));
                codec12.encode(encode, getter12.apply(apply));
                codec13.encode(encode, getter13.apply(apply));
            }
        };
    }

}
