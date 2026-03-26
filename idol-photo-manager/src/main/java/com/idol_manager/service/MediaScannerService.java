package com.idol_manager.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idol_manager.entity.Media;
import com.idol_manager.repository.MediaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaScannerService {

    private final MediaRepository mediaRepository;

    // application.properties で設定したパスを取得
    @Value("${app.scan.folder-path}")
    private String scanFolderPath;

    /**
     * 指定フォルダ内の未登録ファイル数をカウントする
     */
    public long countUnregisteredFiles() {
        File[] files = getMediaFilesFromFolder();
        if (files == null) return 0;

        long unregisteredCount = 0;
        // existsByFilePath: MediaRepository で定義したメソッドを使い、スキャンしたファイルのフルパスが既にDBにあるか1件ずつチェックします。
        for (File file : files) {
            // DBにフルパスが登録されていなければカウント
            if (!mediaRepository.existsByFilePath(file.getAbsolutePath())) {
                unregisteredCount++;
            }
        }
        return unregisteredCount;
    }

    /**
     * 未登録ファイルをデータベースに仮登録（パスのみ保存）する
     */
    // 仮登録: この段階では member_id や location_id は null のまま保存されます。これにより、フロントエンドの「未登録一覧」に表示されるようになります。
    // @Transactional: registerNewFiles メソッドに付けることで、一括登録中にエラーが起きてもデータの整合性を保ちます。
    @Transactional
    public void registerNewFiles() {
        File[] files = getMediaFilesFromFolder();
        if (files == null) return;

        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            
            // 重複チェック
            if (!mediaRepository.existsByFilePath(absolutePath)) {
                Media media = new Media();
                media.setFilePath(absolutePath);
                // 初期状態として登録
                mediaRepository.save(media);
            }
        }
    }

    /**
     * ローカルフォルダから画像・動画ファイルを取得する内部メソッド
     */
    private File[] getMediaFilesFromFolder() {
        File folder = new File(scanFolderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return new File[0];
        }

        // 拡張子 (jpg, png, mp4, movなど) でフィルタリング
        // 拡張子フィルタ: Googleフォトから落ちてくる一般的な静止画・動画形式を対象にしています。
        return folder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".jpg") || 
                   lowerName.endsWith(".jpeg") || 
                   lowerName.endsWith(".png") || 
                   lowerName.endsWith(".mp4") || 
                   lowerName.endsWith(".mov");
        });
    }
}
