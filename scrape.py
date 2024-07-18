import csv
import math
import random
import sys
import time

import requests

def save_csv(filename, content):
    "保存数据为CSV文件 list 写入"
    fp = open(f'{filename}.csv', 'a+', newline='', encoding='utf-8-sig')
    csv_fp = csv.writer(fp)
    csv_fp.writerow(content)
    fp.close()
    print(f'正在写入:{content}')


header = {
    'Referer': 'https://category.vip.com/',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.79',
    'Cookie': "vip_cps_cuid=CU17211997887043d725c0303d477baa; vip_cps_cid=1721199788707_84a55b170449c5631e44d1a948c42540; cps_share=cps_share; PAPVisitorId=2e9e15a743561a9937216a958fef5910; vip_new_old_user=1; vip_city_name=%E5%B9%BF%E5%B7%9E%E5%B8%82; mst_area_code=104104; mars_cid=1721199793765_bf86f830a39bff825d3d077dcfb9fabe; mars_pid=0; pc_fdc_area_id=104102101; pc_fdc_source_ip=1; vip_address=%257B%2522pname%2522%253A%2522%255Cu6e56%255Cu5317%255Cu7701%2522%252C%2522pid%2522%253A%2522104102%2522%252C%2522cname%2522%253A%2522%255Cu5e7f%255Cu5dde%255Cu5e02%2522%252C%2522cid%2522%253A%2522104102101%2522%257D; vip_province=104102; vip_province_name=%E6%B9%96%E5%8C%97%E7%9C%81; vip_city_code=104102101; vip_wh=VIP_HZ; vip_ipver=31; vip_access_times=%7B%22list%22%3A4%2C%22detail%22%3A4%7D; VipDFT=0; cps=adp%3Antq8exyc%3A%40_%401721264729483%3Amig_code%3A4f6b50bf15bfa39639d85f5f1e15b10f%3Aac014miuvl0000b5sq8ccg9z8ejqifm3; mars_sid=c4fb46758fce3a027f4ddf49f4ceaf90; visit_id=0EB48C8F66199D28AA812519F8035F14; vipshop_passport_src=https%3A%2F%2Fwww.vip.com%2F; _jzqco=%7C%7C%7C%7C%7C1.213530716.1721264894587.1721264894587.1721264894587.1721264894587.1721264894587.0.0.0.1.1; PASSPORT_ACCESS_TOKEN=4D14D5862FEDCA5BD01B29DCC212BBE00396BF2F; VipRUID=598925961; VipUID=f22eba98fedc838b0ec7091405895d9c; VipRNAME=ph_*****************************1e4; VipLID=0%7C1721265034%7C19d3dc; VipDegree=D1; user_class=b; VipUINFO=luc%3Ab%7Csuc%3Ab%7Cbct%3Ac_new%7Chct%3Ac_new%7Cbdts%3A0%7Cbcts%3A0%7Ckfts%3A0%7Cc10%3A0%7Crcabt%3A0%7Cp2%3A0%7Cp3%3A1%7Cp4%3A0%7Cp5%3A1%7Cul%3A3105; vpc_uinfo=fr1352%3A0%2Cfr674%3AD1%2Cfr766%3A0%2Cfr896%3A0%2Cfr392%3A310505%2Cfr398%3A0%2Cfr408%3A0%2Cfr1195%3A0%2Cfr251%3AB%2Cfr848%3A0%2Cfr1196%3A0%2Cfr902%3A0%2Cfr901%3A0%2Cfr980%3A0%2Cfr1570%3A0%2Cfr713%3A0%2Cfr1575%3A0%2Cfr1051%3A0%2Cfr259%3AS0-4%2Cfr1655%3A0%2Cfr884%3A0%2Cfr863%3A0%2Cfr249%3AB%2Cfr1527%3A0%2Cfr344%3A0%2Cfr328%3A3105%2Cfr1544%3A0%2Cfr1543%3A0%2Cfr1521%3A0; pg_session_no=8; vip_tracker_source_from=; waitlist=%7B%22pollingId%22%3A%222B379DFA-4DD7-453C-A246-A558AD84D3C7%22%2C%22pollingStamp%22%3A1721265045997%7D"
}


def get_page(pageOffset):
    link = f'https://mapi.vip.com/vips-mobile/rest/shopping/pc/search/product/rank?app_name=shop_pc&app_version=4.0&warehouse=VIP_SH&fdc_area_id=103102101&client=pc&mobile_platform=1&province_id=103102&api_key=70f71280d5d547b2a7bb370a529aeea1&user_id=&mars_cid=1689250429110_19a247e6d39524f0b2691fcad2474761&wap_consumer=a&standby_id=nature&keyword=洗衣液&lv3CatIds=&lv2CatIds=&lv1CatIds=&brandStoreSns=&props=&priceMin=&priceMax=&vipService=&sort=0&pageOffset={pageOffset}&channelId=1&gPlatform=PC&batchSize=120&_=1689316409880'
    page_num = math.ceil(pageOffset / 120) + 1
    print(f'正在爬取第{page_num}页')
    print('pageOffset', f'{pageOffset}')
    # print(link)

    response = requests.get(url=link, headers=header)

    if response.status_code == 200:
        # print(response.text)
        products = response.json()['data']['products']

        page_total = response.json()['data']['total']
        print('products', products)  # 商品ID列表
        print('len(products)', len(products))

        '''# products_list = []
        for products_list in products:
            print(products_list['pid'])'''

        pid_lists = [product['pid'] for product in products]

        pid_list1 = pid_lists[:50]
        pid_list2 = pid_lists[50:100]
        pid_list3 = pid_lists[100:]

        pid_list = [pid_list1, pid_list2, pid_list3]

        # for pid in pid_list:
        for i, pid in enumerate(pid_list):
            print('此子页面的商品pid', pid)  # 切片的商品ID列表
            print('len(pid)', len(pid))
            pid_string = ','.join(pid)
            # print('pid_string', pid_string)

            url = f'https://mapi.vip.com/vips-mobile/rest/shopping/pc/product/module/list/v2'
            #       https://mapi.vip.com/vips-mobile/rest/shopping/pc/product/module/list/v2
            params = {
                'app_name': ' shop_pc',
                'app_version': ' 4.0',
                'warehouse': ' VIP_SH',
                'fdc_area_id': ' 103102101',
                'client': ' pc',
                'mobile_platform': ' 1',
                'province_id': ' 103102',
                'api_key': ' 70f71280d5d547b2a7bb370a529aeea1',
                'user_id': ' ',
                'mars_cid': ' 1689250429110_19a247e6d39524f0b2691fcad2474761',
                #             1689250429110_19a247e6d39524f0b2691fcad2474761
                'wap_consumer': ' a',
                'productIds': pid_string,
                # 'productIds':' 6919967848692122900,6919798151514518861,6919172521165554132,6920285508803059228,6920086968587152079,6919986635456665227,6919250795635778132,6919798151514506573,6919221354152415892,6920333096156845268,6919570823605344796,6919675086681596703,6919938807385331604,6920323328092155399,6920224576466925191,6920269775151187284,6920137626444862100,6919073646414612043,6919798151565043021,6919269290708597831,6920404534151400320,6920333096156861652,6920453816872247898,6920292798115451289,6920377028907999367,6920330580854132231,6920311638011276941,6920101520475945679,6920189634053621524,6918915874224063836,6920224576466912903,6920334927451704839,6919827210032574940,6919764953830565520,6920403031213253504,6919259179854452052,6920371115020341959,6920386923071996608,6920397091090039620,6919837579291338844,6919136056805686164,6920334927451713031,6920430261628644295,6918700873957560468,6919675341290738836,6920250903815424655,6920366918282336732,6920346658809823645,2169989739,6920453816872116826,',
                'scene': ' search',
                'standby_id': ' nature',
                'extParams': ' {"stdSizeVids":"","preheatTipsVer":"3","couponVer":"v2","exclusivePrice":"1","iconSpec":"2x","ic2label":1,"superHot":1,"bigBrand":"1"}',
                'context': ' ',
                '_': ' 1689309574409'

            }

            response = requests.get(url=url, params=params, headers=header)
            # print(response)

            if response.json().get('data') and response.status_code == 200:
                print('response.json()', response.json())
                products_info = response.json()['data']['products']
                # print('products_info', products_info)

                for product in products_info:  # 爬取第一页的3个子页面商品信息
                    # print(product)
                    '''for j in product['attrs']:
                        print(j)
                        attr = j['value']
                        ','.join(attr)'''
                    attr = ','.join([j['value'] for j in product['attrs']])  # 商品信息
                    brandId = product['brandId']  # 品牌id
                    brandShowName = product['brandShowName']  # 品牌名
                    marketPrice = product['price']['marketPrice']  # 原价
                    mixPriceLabel = product['price']['mixPriceLabel']  # 折扣
                    salePrice = product['price']['salePrice']  # 现价
                    productId = product['productId']  # 商品id
                    skuId = product['skuId']  # skuId
                    title = product['title']  # 标题
                    #squareImage = product['squareImage']
                    smallImage = product['smallImage']
                    #satisfaction = data['satisfaction']

                    # print(title, attr, brandId, brandShowName, productId, marketPrice, salePrice, mixPriceLabel, skuId)

                    # 标题title，商品信息attr，品牌id brandId，品牌名brandShowName，商品id productId
                    # 原价marketPrice，现价salePrice，折扣mixPriceLabel，skuId
                    info_col = [title, attr, brandId, brandShowName, productId, marketPrice, salePrice, mixPriceLabel,
                                skuId,smallImage]
                    save_csv('filename', info_col)

            else:
                print(f'请求第{page_num}页商品第{i + 1}个子页面失败', '不存在该商品', '\n')
                with open('请求失败数据.csv', mode='a+', newline='', encoding='utf-8-sig') as file:
                    writer = csv.writer(file)
                    sentence = f'请求第{page_num}页商品第{i + 1}个子页面失败'
                    writer.writerow([sentence])
                file.close()

            print(f'共有{page_total}条数据,第{page_num}页，第{i + 1}个子页面商品信息({page_num}_{i + 1})-->已保存')

            # 暂停6-20秒
            t = random.randint(12, 30)
            if i == 0 or i == 1:
                print(f'休息{t}秒后，继续爬取第{page_num}整页的下个子页（第{page_num}_{i + 2}页）评论', '\n')
            else:
                print(f'休息{t}秒后，准备爬取第{page_num + 1}页，完整页评论', '\n')
            time.sleep(t)

        print(f'第{page_num}页商品信息-->已保存', '\n', '\n')

        # 总页数 = math.ceil(page_total / 120)  # 向上取整  2241/120=18.675  19页
        if page_num > math.floor(page_total / 480):  # math.floor(page_total / 120) 向下取整  # 如果当前页数大于总页数，就停止爬取
            print('爬取完毕')
            sys.exit()  # 退出程序
        else:  # 否则，继续爬取下一页
            page_num += 1
            pageOffset += 120

            get_page(pageOffset)  # 递归调用

    else:
        print(f'请求失败第{page_num}页失败')


if __name__ == '__main__':
    get_page(0)
