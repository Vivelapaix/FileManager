package filemanager.preview;

import filemanager.exceptions.ExceptionHandler;
import filemanager.view.FileManagerView;
import filemanager.view.FilePreviewView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.testng.Assert.assertEquals;


public class CreatePreviewTest {

    private ExceptionHandler exceptionHandler;

    private FileManagerView view;

    private PreviewFactory factory;

    @BeforeMethod
    public void before() {
        factory = new PreviewFactory();
        view = createMock(FileManagerView.class);
        exceptionHandler = createMock(ExceptionHandler.class);

        FilePreviewView preview = createMockBuilder(FilePreviewView.class).createMock();

        expect(view.getFilePreview()).andReturn(preview).anyTimes();
        replay(view);
    }

    @Test
    public void shouldSeeEmptyPreviewForDirectory() {
        File file = createMock(File.class);
        expect(file.isDirectory()).andReturn(true);
        replay(file);

        Preview preview = factory.createPreview(view, file, exceptionHandler);

        assertEquals(preview.getClass(), EmptyPreview.class);
    }


    @DataProvider(name = "textFiles")
    public static Object[][] textFiles() {
        return new Object[][] {
                {"file.txt"}, {"file.html"}, {"file.java"},
                {"file.css"}, {"file.js"}, {"file.xml"},
                {"file.py"}, {"file.cpp"}, {"file.csv"},
                {"file.rb"}, {"file.c"}};
    }

    @Test(dataProvider = "textFiles")
    public void shouldSeeTextPreviewForTextFiles(String textFile) {
        File file = createMock(File.class);

        expect(file.isDirectory()).andReturn(false);
        expect(file.getName()).andReturn(textFile).anyTimes();
        replay(file);

        Preview preview = factory.createPreview(view, file, exceptionHandler);

        assertEquals(preview.getClass(), TextPreview.class);
    }

    @DataProvider(name = "imageFiles")
    public static Object[][] imageFiles() {
        return new Object[][] {
                {"file.png"}, {"file.jpeg"}, {"file.jpg"},
                {"file.PNG"}, {"file.JPEG"}, {"file.JPG"}
        };
    }

    @Test(dataProvider = "imageFiles")
    public void shouldSeeImagePreviewForImageFiles(String imageFile) {
        File file = createMock(File.class);
        expect(file.isDirectory()).andReturn(false);
        expect(file.getName()).andReturn(imageFile).anyTimes();
        replay(file);

        Preview preview = factory.createPreview(view, file, exceptionHandler);

        assertEquals(preview.getClass(), ImagePreview.class);
    }

    @DataProvider(name = "unknownFiles")
    public static Object[][] unknownFiles() {
        return new Object[][] {
                {"file.mp3"}, {"file.mp4"}, {"file.djvu"},
                {"file.pdf"}, {"file.sql"}, {"file.ipynb"},
                {"file.doc"}, {"file.docx"}
        };
    }

    @Test(dataProvider = "unknownFiles")
    public void shouldSeeEmptyPreviewForUnknownFile(String unknownFile) {
        File file = createMock(File.class);

        expect(file.isDirectory()).andReturn(false);
        expect(file.getName()).andReturn(unknownFile).anyTimes();
        replay(file);

        Preview preview = factory.createPreview(view, file, exceptionHandler);

        assertEquals(preview.getClass(), EmptyPreview.class);
    }
}
