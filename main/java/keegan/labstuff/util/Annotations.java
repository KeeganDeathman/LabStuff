package keegan.labstuff.util;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Annotations
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface RuntimeInterface
	{
		String clazz();

		String modID();

        String deobfName() default "";
	      
		String[] altClasses() default {};
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface AltForVersion
	{
		String version();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface NetworkedField
	{
		Side targetSide();
	}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface VersionSpecific
    {
        String version();
    }
}