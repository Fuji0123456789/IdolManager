package com.idol_manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.idol_manager.repository.MediaRepository;

class MediaScannerServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @InjectMocks
    private MediaScannerService mediaScannerService;

    // テスト用の一次的なフォルダ（テスト終了後に自動削除される）
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // @Value("${app.scan.folder-path}") の値をテスト用フォルダに上書き
        ReflectionTestUtils.setField(mediaScannerService, "scanFolderPath", tempDir.toString());
    }

    @Test
    void testCountUnregisteredFiles() throws IOException {
        // 1. テスト用のファイルを2つ作成
        Files.createFile(tempDir.resolve("idol1.jpg"));
        Files.createFile(tempDir.resolve("idol2.png"));
        
        String path1 = tempDir.resolve("idol1.jpg").toAbsolutePath().toString();
        String path2 = tempDir.resolve("idol2.png").toAbsolutePath().toString();

        // 2. Mockの設定：1つ目は登録済み、2つ目は未登録とする
        when(mediaRepository.existsByFilePath(path1)).thenReturn(true);
        when(mediaRepository.existsByFilePath(path2)).thenReturn(false);

        // 3. 実行
        long count = mediaScannerService.countUnregisteredFiles();

        // 4. 検証：未登録は1件のはず
        assertEquals(1, count);
    }

    @Test
    void testRegisterNewFiles() throws IOException {
        // 1. テスト用のファイルを作成
        Files.createFile(tempDir.resolve("new_photo.jpg"));
        String path = tempDir.resolve("new_photo.jpg").toAbsolutePath().toString();

        // 2. Mockの設定：未登録状態にする
        when(mediaRepository.existsByFilePath(path)).thenReturn(false);

        // 3. 実行
        mediaScannerService.registerNewFiles();

        // 4. 検証：saveが呼ばれたか確認
        verify(mediaRepository, times(1)).save(any());
    }
}
