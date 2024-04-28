package com.swygbro.trip.backend.domain.guideProduct.fixture;

import com.swygbro.trip.backend.domain.guideProduct.domain.GuideCategoryCode;
import com.swygbro.trip.backend.domain.guideProduct.dto.CreateGuideProductRequest;

import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CreateGuideProductRequestFixture {

    public static CreateGuideProductRequest getGuideProductRequest() {
        List<GuideCategoryCode> categories = new ArrayList<>();
        categories.add(GuideCategoryCode.C1);
        categories.add(GuideCategoryCode.C2);

        return new CreateGuideProductRequest(
                "account1",
                "test title",
                "test description",
                20000L,
                100.1,
                50.2,
                Year.of(2024).atMonth(4).atDay(28).atTime(23, 0).atZone(ZoneId.of("Asia/Seoul")),
                Year.of(2024).atMonth(4).atDay(28).atTime(23, 0).atZone(ZoneId.of("Asia/Seoul")),
                categories
        );
    }
}
