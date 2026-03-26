package com.idol_manager.controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.idol_manager.dto.MediaUpdateForm;
import com.idol_manager.entity.Media;
import com.idol_manager.entity.Member;
import com.idol_manager.repository.EventRepository;
import com.idol_manager.repository.IdolGroupRepository;
import com.idol_manager.repository.LocationRepository;
import com.idol_manager.repository.MediaRepository;
import com.idol_manager.repository.MediaTypeRepository;
import com.idol_manager.repository.MemberRepository;
import com.idol_manager.service.MediaScannerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MediaController {

    // フィールド
    private final MediaScannerService mediaScannerService;
    private final MediaRepository mediaRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final MediaTypeRepository mediaTypeRepository;
	private final IdolGroupRepository idolGroupRepository;
	private final MemberRepository memberRepository;

    /**
     * ログイン後のトップ画面（未登録件数の表示）
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // フォルダ内をスキャンして未登録の件数を取得
        long unregisteredCount = mediaScannerService.countUnregisteredFiles();
        
        model.addAttribute("unregisteredCount", unregisteredCount);
        return "dashboard"; // dashboard.html を表示
    }

    /**
     * 未登録ファイルをDBに登録し、一覧画面へ遷移する
     */
    @PostMapping("/media/register-and-list")
    public String registerAndShowList() {
        // 未登録ファイルをDBへ一括保存（パスのみ）
    	// POSTリクエスト: registerNewFiles はDBの状態を更新するため、安全のために GET ではなく POST で呼び出す設計にしています。
        mediaScannerService.registerNewFiles();
        
        // 登録完了後、動画像一覧画面へリダイレクト
        // リダイレクト: 登録処理が終わった後は redirect: を使うことで、ブラウザの「更新ボタン」による二重登録を防ぎます。
        return "redirect:/media/list";
    }

    /**
     * 動画像一覧表示画面
     */
    @GetMapping("/media/list")
    public String showMediaList(Model model) {
        // DBに登録されている全件数を取得
    	// totalCount: 一覧画面で「これまで取得した動画像の件数」を表示する要件に対応しています。
        //long totalCount = mediaRepository.count();
        
        model.addAttribute("mediaList", mediaRepository.findAll());
        //model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalCount", mediaRepository.count());
        
        // 編集用の選択肢（マスタ）をすべて渡す
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("events", eventRepository.findAll());
        model.addAttribute("types", mediaTypeRepository.findAll());
        
        return "media_list"; // media_list.html を表示
    }

    @PostMapping("/media/update")
    public String updateMedia(MediaUpdateForm form) {
        // デバッグ用：IDが来ているかコンソールに出力して確認
        System.out.println("Updating Media ID: " + form.getMediaId());

        if (form.getMediaId() == null) {
            // IDがない場合は一覧に戻す（エラーにしない）
            return "redirect:/media/list";
        }

        Media media = mediaRepository.findById(form.getMediaId()).orElseThrow();
        
        // 基本情報のセット
        // --- 修正箇所：空文字チェックを追加 ---
        String dateStr = form.getAcquiredDate();
        if (dateStr != null && !dateStr.isEmpty()) {
            media.setAcquiredDate(java.time.LocalDate.parse(dateStr));
        } else {
            media.setAcquiredDate(null); // 未入力の場合はnullをセット（DBで許可している場合）
        }
        
        // locationId
        //media.setLocation(locationRepository.findById(form.getLocationId()).orElse(null));
        // 2. IDがnullでない場合のみリポジトリから検索してセットする
        media.setLocation(form.getLocationId() != null 
            ? locationRepository.findById(form.getLocationId()).orElse(null) 
            : null);

        // eventId (ここが105行目前後でエラーになっている可能性が高いです)
        //media.setEvent(eventRepository.findById(form.getEventId()).orElse(null));
        media.setEvent(form.getEventId() != null 
            ? eventRepository.findById(form.getEventId()).orElse(null) 
            : null);
        
        // typeId
        //media.setType(mediaTypeRepository.findById(form.getTypeId()).orElse(null));
        media.setType(form.getTypeId() != null 
            ? mediaTypeRepository.findById(form.getTypeId()).orElse(null) 
            : null);
        
        // 複数メンバーの紐付け更新
        //List<Member> selectedMembers = memberRepository.findAllById(form.getMemberIds());
        List<Member> selectedMembers = (form.getMemberIds() != null) 
                ? memberRepository.findAllById(form.getMemberIds()) 
                : new ArrayList<>(); // 選択がない場合は空のリスト
        media.setMembers(new HashSet<>(selectedMembers));
        
        mediaRepository.save(media);
        return "redirect:/media/list";
    }
}
