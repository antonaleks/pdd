package org.antonaleks.pdd.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.antonaleks.pdd.utils.ResourceHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageDeserializer extends StdDeserializer<String> {


    public ImageDeserializer() {
        this(null);
    }

    public ImageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        String path = parser.getText();
        ResourceHelper app = new ResourceHelper();
        try {
            InputStream is = app.getFileFromResourceAsStream("assets/images/" + path);

            BufferedImage bImage = ImageIO.read(is);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos);
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (IllegalArgumentException e) {
            return path;
        }

    }
}