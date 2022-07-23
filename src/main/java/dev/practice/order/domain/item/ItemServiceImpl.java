package dev.practice.order.domain.item;

import dev.practice.order.domain.partner.PartnerReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final PartnerReader partnerReader;
    private final ItemStore itemStore;
    private final ItemReader itemReader;
    private final ItemOptionSeriesFactory itemOptionSeriesFactory;

    /** Item 전체 Aggregate를 저장하는 부분 */
    @Override
    @Transactional
    public String registerItem(ItemCommand.RegisterItemRequest command, String partnerToken) {
        var partner = partnerReader.getPartner(partnerToken);
        var initItem = command.toEntity(partner.getId());
        var item = itemStore.store(initItem);
        //Factory를 쓰지 않는다면, Foreach를 돌면서, ItemOptionGroup과 ItemOption을 저장할 것
        //그런데, ItemServiceImpl에서, 추상화 레벨이 낮아지는 문제가 있음. --> 그래서 Factory를 씀
        itemOptionSeriesFactory.store(command, item);
        return item.getItemToken();
    }

    @Override
    @Transactional
    public void changeOnSale(String itemToken) {
        var item = itemReader.getItemBy(itemToken);
        item.changeOnSale();
    }

    @Override
    @Transactional
    public void changeEndOfSale(String itemToken) {
        var item = itemReader.getItemBy(itemToken);
        item.changeEndOfSale();
    }


    @Override
    @Transactional(readOnly = true)
    public ItemInfo.Main retrieveItemInfo(String itemToken) {
        //ItemToken으로 item을 가져온 후
        var item = itemReader.getItemBy(itemToken);
        var itemOptionGroupInfoList = itemReader.getItemOptionSeries(item);
        //리턴 시에는 XxxInfo 형태로 리턴. 
        return new ItemInfo.Main(item, itemOptionGroupInfoList);
    }
}
